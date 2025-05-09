package com.booking.bookingapp.service.booking;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import com.booking.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.booking.bookingapp.exception.BookingException;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.mapper.AddressMapper;
import com.booking.bookingapp.mapper.AmenitiesMapper;
import com.booking.bookingapp.mapper.BookingMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.booking.BookingSpecificationBuilder;
import com.booking.bookingapp.service.notification.NotificationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private static final Long ONE_DAY = 1L;
    private static final Long ZERO = 0L;

    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AmenitiesRepository amenitiesRepository;
    private final AmenitiesMapper amenitiesMapper;
    private final BookingSpecificationBuilder bookingSpecificationBuilder;
    private final NotificationService notificationService;

    @Value("${site.url}")
    private String url;

    @Override
    public BookingResponseDto save(BookingRequestDto requestDto,
                                   UriComponentsBuilder uri, User user) {
        checkPendingPaymentBooking(user.getId());

        Accommodation accommodation = getAccommodationById(requestDto.getAccommodationId());
        checkAccommodationAvailability(accommodation);

        Booking booking = bookingMapper.toModel(requestDto, accommodation, user);
        bookingRepository.save(booking);

        BookingResponseDto bookingResponseDto = bookingMapper.toDto(booking);

        notificationService.createdBooking(bookingResponseDto, uri);
        return bookingResponseDto;
    }

    @Override
    public List<BookingResponseDto> search(BookingSearchParameters searchParameters,
                                           Pageable pageable) {
        Specification<Booking> bookingSpecification = bookingSpecificationBuilder
                .build(searchParameters);
        return bookingRepository.findAll(bookingSpecification, pageable).stream()
                .map(bookingMapper::toDto)
                .toList();
    }

    @Override
    public List<BookingResponseDto> getByUser(Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Booking> bookings = bookingRepository.findBookingsByUser(user, pageable);

        return bookings.stream().map(bookingMapper::toDto).toList();
    }

    @Override
    public BookingResponseDto getById(Long id) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bookingMapper.toDto(getBookingById(id, user));
    }

    @Override
    public BookingResponseDto updateById(Long id, UpdateBookingRequestDto requestDto,
                                         UriComponentsBuilder uriBuilder) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Booking booking = getBookingById(id, user);

        bookingMapper.updateModel(requestDto, booking);

        Accommodation accommodation = getAccommodationById(requestDto.getAccommodationId());

        if (unchangedAccommodation(accommodation, booking)) {
            booking.setAccommodation(accommodation);
        } else {
            checkAccommodationAvailability(accommodation);

            booking.setAccommodation(accommodation);

            notificationService.releasedAccommodation(
                    accommodationMapper.toDto(accommodation), uriBuilder);
        }

        bookingRepository.save(booking);
        BookingResponseDto bookingResponseDto = bookingMapper.toDto(booking);

        if (bookingResponseDto.getStatus().equals(Booking.Status.CANCELED)) {
            notificationService.canceledBooking(
                    user.getTelegramId(),
                    bookingResponseDto,
                    uriBuilder,
                    bookingResponseDto.getStatus());
            notificationService.releasedAccommodation(
                    bookingResponseDto.getAccommodation(), uriBuilder);
        }

        return bookingResponseDto;
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }

    @Scheduled(cron = "0 00 12 * * *")
    protected void processExpiredBookings() {
        LocalDate tomorrow = LocalDate.now().plusDays(ONE_DAY);
        List<Booking.Status> statuses = List.of(Booking.Status.PENDING, Booking.Status.CONFIRMED);

        List<Booking> bookings = bookingRepository
                .findAllByCheckOutDateLessThanEqualAndStatusIn(tomorrow, statuses);

        if (bookings.isEmpty()) {
            notificationService.notifyNoExpiredBookings();
        } else {
            bookings.forEach(booking -> {
                booking.setStatus(Booking.Status.EXPIRED);
                bookingRepository.save(booking);

                notificationService.releasedAccommodation(
                        accommodationMapper.toDto(booking.getAccommodation()),
                        UriComponentsBuilder.fromUriString(url));
            });
        }
    }

    private void checkPendingPaymentBooking(Long userId) {
        log.debug("Count pending booking: {}",
                bookingRepository.countAllByStatusAndUserId(Booking.Status.PENDING, userId));
        if (bookingRepository.countAllByStatusAndUserId(Booking.Status.PENDING, userId)
                .orElse(ZERO) > ZERO) {
            throw new BookingException("Payment or cancellation is expected");
        }
    }

    private Accommodation getAccommodationById(Long id) {
        return accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found accommodation by id: " + id));
    }

    private List<AmenitiesResponseDto> getAllAmenitiesDto(Set<Amenities> amenitiesList) {
        return amenitiesList.stream()
                .map(amenities -> amenitiesRepository.findById(amenities.getId())
                        .orElseThrow(() -> new EntityNotFoundException(
                                "Can't found amenities by id: " + amenities.getId()))
                )
                .map(amenitiesMapper::toDto)
                .toList();
    }

    private Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found address by id: " + id));
    }

    private Booking getBookingById(Long id,User user) {
        return bookingRepository.findByIdAndUser(id, user).orElseThrow(
                () -> new EntityNotFoundException("Can't found booking by id: " + id));
    }

    private void checkAccommodationAvailability(Accommodation accommodation) {
        List<Booking.Status> statuses = List.of(Booking.Status.CONFIRMED, Booking.Status.PENDING);
        Long count = bookingRepository.countAllByAccommodationIdAndStatuses(
                accommodation.getId(), statuses).orElse(ZERO);

        log.debug("Count accommodation : {}", count);

        if (accommodation.getAvailability()
                < count) {
            throw new BookingException("Not availability accommodation");
        }
    }

    private boolean unchangedAccommodation(Accommodation accommodation, Booking booking) {
        return booking.getAccommodation().getId().equals(accommodation.getId());
    }
}
