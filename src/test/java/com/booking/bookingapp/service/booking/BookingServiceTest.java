package com.booking.bookingapp.service.booking;

import static com.booking.bookingapp.model.Role.RoleName.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
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
import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.booking.BookingSpecificationBuilder;
import com.booking.bookingapp.service.notification.NotificationService;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String STATUS = "PENDING";
    private static final int PAGE_SIZE = 10;
    private static final int INDEX = 0;
    private static final int ACTUAL_SIZE = 1;
    private static final Long ONE_DAY = 1L;
    private static final int AVAILABILITY = 10;

    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private AccommodationRepository accommodationRepository;
    @Mock
    private AmenitiesRepository amenitiesRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private AccommodationMapper accommodationMapper;
    @Mock
    private AmenitiesMapper amenitiesMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private BookingSpecificationBuilder bookingSpecificationBuilder;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private BookingRequestDto bookingRequestDto;
    private User user;
    private Accommodation accommodation;
    private AccommodationResponseDto accommodationResponseDto;
    private AmenitiesResponseDto amenitiesResponseDto;
    private Amenities amenities;
    private Address address;
    private AddressResponseDto addressResponseDto;
    private Booking booking;
    private BookingResponseDto bookingResponseDto;
    private UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromUriString("www.booking.com");
    private Pageable pageable;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field urlField = BookingServiceImpl.class.getDeclaredField("url");
        urlField.setAccessible(true);
        urlField.set(bookingService, uriBuilder.toString());

        pageable = Pageable.ofSize(PAGE_SIZE);

        bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAccommodationId(1L);

        Role adminRole = new Role();
        adminRole.setRole(ADMIN);

        user = new User();
        user.setId(VALID_ID);
        user.setRole(Set.of(adminRole));

        amenities = new Amenities();
        amenities.setId(VALID_ID);

        address = new Address();
        address.setId(VALID_ID);

        addressResponseDto = new AddressResponseDto();
        addressResponseDto.setId(VALID_ID);

        amenitiesResponseDto = new AmenitiesResponseDto();
        amenitiesResponseDto.setId(VALID_ID);

        accommodation = new Accommodation();
        accommodation.setId(VALID_ID);
        accommodation.setAvailability(AVAILABILITY);
        accommodation.setAmenities(Set.of(amenities));
        accommodation.setLocation(address);

        booking = new Booking();
        booking.setId(VALID_ID);
        booking.setAccommodation(accommodation);
        booking.setUser(user);

        accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(VALID_ID);
        accommodationResponseDto.setAmenities(List.of(amenitiesResponseDto));

        bookingResponseDto = new BookingResponseDto();
        bookingRequestDto.setAccommodationId(VALID_ID);

        Authentication authentication = Mockito.mock(Authentication.class);

        lenient().when(authentication.getPrincipal()).thenReturn(user);

        SecurityContext securityContext = mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        lenient().when(addressRepository.findById(VALID_ID)).thenReturn(Optional.of(address));
        lenient().when(addressMapper.toDto(address)).thenReturn(addressResponseDto);
        lenient().when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);
        lenient().when(amenitiesRepository.findById(VALID_ID)).thenReturn(Optional.of(amenities));
        lenient().when(amenitiesMapper.toDto(amenities)).thenReturn(amenitiesResponseDto);
        lenient().when(accommodationRepository.findById(bookingRequestDto.getAccommodationId()))
                .thenReturn(Optional.of(accommodation));
        lenient().when(bookingMapper.toModel(bookingRequestDto)).thenReturn(booking);
        lenient().when(bookingMapper.toDto(any(Booking.class))).thenReturn(bookingResponseDto);
    }

    @Test
    void save_ValidDateBooking_ReturnBookingResponseDto() {
        BookingResponseDto actual = bookingService.save(bookingRequestDto, uriBuilder);

        verify(bookingRepository, times(1))
                .save(booking);
        verify(notificationService, times(1))
                .createdBooking(bookingResponseDto, uriBuilder);

        assertEquals(bookingResponseDto, actual);
    }

    @Test
    void checkPendingPaymentBooking_PaymentExpected_ShouldThrowException() {
        when(bookingRepository.countAllByStatusAndUserId(Booking.Status.PENDING, VALID_ID))
                .thenReturn(Optional.of(1L));

        BookingException exception = assertThrows(BookingException.class,
                () -> bookingService.save(bookingRequestDto, uriBuilder));

        assertEquals("Payment or cancellation is expected", exception.getMessage());
    }

    @Test
    void getAllAmenitiesDto_AmenitiesIdInvalid_ShouldThrowException() {
        amenities.setId(INVALID_ID);

        when(amenitiesRepository.findById(amenities.getId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.save(bookingRequestDto, uriBuilder));

        assertEquals("Can't found amenities by id: " + INVALID_ID, exception.getMessage());
    }

    @Test
    void getAccommodationById_InvalidId_ShouldThrowException() {
        bookingRequestDto.setAccommodationId(INVALID_ID);
        when(accommodationRepository.findById(bookingRequestDto.getAccommodationId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.save(bookingRequestDto, uriBuilder));

        assertEquals("Can't found accommodation by id: " + INVALID_ID, exception.getMessage());
    }

    @Test
    void getAddress_InvalidId_ShouldThrowException() {
        address.setId(INVALID_ID);
        accommodation.setLocation(address);

        when(addressRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> bookingService.save(bookingRequestDto, uriBuilder));
        assertEquals("Can't found address by id: " + INVALID_ID, exception.getMessage());
    }

    @Test
    void checkedAccommodationAvailability_NotAvailable_ShouldThrowException() {
        List<Booking.Status> statuses = List.of(Booking.Status.CONFIRMED, Booking.Status.PENDING);
        bookingRequestDto.setAccommodationId(VALID_ID);
        accommodation.setAvailability(0);

        when(bookingRepository.countAllByAccommodationIdAndStatuses(accommodation.getId(),
                statuses)).thenReturn(Optional.of(1L));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> bookingService.save(bookingRequestDto, uriBuilder));
        assertEquals("Not availability accommodation", exception.getMessage());
        verify(bookingRepository, times(1))
                .countAllByAccommodationIdAndStatuses(accommodation.getId(), statuses);
    }

    @Test
    void search_ValidSearchParameters_ReturnBookingResponseDtos() {
        BookingSearchParameters searchParameters = new BookingSearchParameters(
                new String[]{VALID_ID.toString()},
                new String[]{STATUS}
        );

        Specification<Booking> specification = mock(Specification.class);
        when(bookingSpecificationBuilder.build(searchParameters)).thenReturn(specification);

        List<Booking> bookings = List.of(booking);
        Page<Booking> bookingPage = new PageImpl<>(bookings);
        when(bookingRepository.findAll(specification, pageable)).thenReturn(bookingPage);

        when(bookingMapper.toDto(booking)).thenReturn(bookingResponseDto);
        List<BookingResponseDto> actual = bookingService.search(searchParameters, pageable);

        assertNotNull(actual);
        assertEquals(ACTUAL_SIZE, actual.size());
        assertEquals(bookingResponseDto, actual.get(INDEX));

        verify(bookingSpecificationBuilder, times(1))
                .build(searchParameters);
        verify(bookingRepository, times(1))
                .findAll(specification, pageable);
        verify(bookingMapper, times(1)).toDto(booking);
    }

    @Test
    void getByUser_ValidUserId_ReturnBookingResponseDtos() {
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .thenReturn(user);

        List<Booking> bookings = List.of(booking);
        when(bookingRepository.findBookingsByUser(user, pageable)).thenReturn(bookings);

        List<BookingResponseDto> actual = bookingService.getByUser(pageable);

        assertNotNull(actual);
        assertEquals(ACTUAL_SIZE, actual.size());
        assertEquals(bookingResponseDto, actual.get(INDEX));

        verify(SecurityContextHolder.getContext().getAuthentication(),
                times(1)).getPrincipal();
        verify(bookingRepository, times(1))
                .findBookingsByUser(user, pageable);
        verify(bookingMapper, times(1)).toDto(booking);
    }

    @Test
    void getById_ValidBookingId_ReturnBookingResponseDto() {
        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));

        BookingResponseDto actual = bookingService.getById(VALID_ID);

        assertNotNull(actual);
        assertEquals(bookingResponseDto, actual);

        verify(bookingRepository, times(1)).findById(VALID_ID);
        verify(bookingMapper, times(1)).toDto(booking);
    }

    @Test
    void getById_InvalidBookingId_NotOk() {
        when(bookingRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookingService.getById(INVALID_ID));

        verify(bookingRepository, times(1)).findById(INVALID_ID);
        verifyNoInteractions(bookingMapper);
    }

    @Test
    void updateById() {
        UpdateBookingRequestDto updateBookingRequestDto = new UpdateBookingRequestDto();
        updateBookingRequestDto.setCheckInDate(LocalDate.now());
        updateBookingRequestDto.setCheckOutDate(LocalDate.now().plusDays(ONE_DAY));
        updateBookingRequestDto.setStatus(Booking.Status.PENDING);
        updateBookingRequestDto.setAccommodationId(VALID_ID);

        Accommodation updateAccommodation = new Accommodation();
        updateAccommodation.setId(VALID_ID);
        updateAccommodation.setAvailability(AVAILABILITY);

        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));
        when(accommodationRepository.findById(VALID_ID))
                .thenReturn(Optional.of(updateAccommodation));

        bookingResponseDto.setStatus(Booking.Status.PENDING);

        BookingResponseDto actual
                = bookingService.updateById(VALID_ID, updateBookingRequestDto, uriBuilder);

        assertNotNull(actual);
        assertEquals(bookingResponseDto, actual);

        verify(bookingRepository, times(1)).findById(VALID_ID);
        verify(accommodationRepository, times(1)).findById(VALID_ID);
        verify(bookingMapper, times(1)).toDto(booking);
        verify(bookingRepository, times(1)).save(booking);
        verifyNoInteractions(notificationService);
    }

    @Test
    void updateById_ChangeAccommodation_CheckAvailabilityAndSendNotification() {
        UpdateBookingRequestDto updateBookingRequestDto = new UpdateBookingRequestDto();
        updateBookingRequestDto.setAccommodationId(VALID_ID + VALID_ID);
        updateBookingRequestDto.setStatus(Booking.Status.PENDING);
        updateBookingRequestDto.setCheckInDate(LocalDate.now());
        updateBookingRequestDto.setCheckOutDate(LocalDate.now().plusDays(ONE_DAY));

        Accommodation newAccommodation = new Accommodation();
        newAccommodation.setId(VALID_ID + VALID_ID);
        newAccommodation.setAvailability(AVAILABILITY);

        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));
        when(accommodationRepository.findById(updateBookingRequestDto.getAccommodationId()))
                .thenReturn(Optional.of(newAccommodation));

        bookingResponseDto.setStatus(Booking.Status.PENDING);

        BookingResponseDto actual
                = bookingService.updateById(VALID_ID, updateBookingRequestDto, uriBuilder);

        assertNotNull(actual);
        assertEquals(bookingResponseDto, actual);

        verify(bookingRepository, times(1)).findById(VALID_ID);
        verify(accommodationRepository, times(1))
                .findById(updateBookingRequestDto.getAccommodationId());
        verify(notificationService, times(1))
                .releasedAccommodation(accommodationMapper.toDto(newAccommodation), uriBuilder);
    }

    @Test
    void updateById_StatusCanceled_SendCancellationAndReleaseNotification() {
        UpdateBookingRequestDto updateBookingRequestDto = new UpdateBookingRequestDto();
        updateBookingRequestDto.setStatus(Booking.Status.CANCELED);
        updateBookingRequestDto.setAccommodationId(VALID_ID);

        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));
        when(accommodationRepository.findById(VALID_ID)).thenReturn(Optional.of(accommodation));

        bookingResponseDto.setStatus(Booking.Status.CANCELED);

        BookingResponseDto actual
                = bookingService.updateById(VALID_ID, updateBookingRequestDto, uriBuilder);

        assertNotNull(actual);
        assertEquals(bookingResponseDto, actual);

        verify(bookingRepository, times(1)).findById(VALID_ID);
        verify(bookingMapper, times(1)).toDto(booking);
        verify(notificationService, times(1)).canceledBooking(
                user.getTelegramId(),
                bookingResponseDto,
                uriBuilder,
                Booking.Status.CANCELED
        );
        verify(notificationService, times(1)).releasedAccommodation(
                bookingResponseDto.getAccommodation(), uriBuilder);
    }

    @Test
    void deleteById_ValidBookingId() {
        bookingService.deleteById(VALID_ID);

        verify(bookingRepository, times(1)).deleteById(VALID_ID);
    }

    @Test
    void processExpiredBookings_NoExpiredBookings_NotifiesNoExpiredBookings() {
        LocalDate tomorrow = LocalDate.now().plusDays(ONE_DAY);
        List<Booking.Status> statuses = List.of(Booking.Status.PENDING, Booking.Status.CONFIRMED);

        when(bookingRepository.findAllByCheckOutDateLessThanEqualAndStatusIn(tomorrow, statuses))
                .thenReturn(List.of());

        bookingService.processExpiredBookings();

        verify(notificationService, times(1)).notifyNoExpiredBookings();
        verifyNoMoreInteractions(notificationService);
        verifyNoInteractions(accommodationMapper);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void processExpiredBookings_ExpiredBookings_UpdatesStatusAndSendsNotifications() {
        accommodation.setId(VALID_ID);

        booking.setStatus(Booking.Status.PENDING);
        booking.setCheckOutDate(LocalDate.now());
        booking.setAccommodation(accommodation);

        bookingResponseDto.setId(VALID_ID);

        List<Booking.Status> statuses = List.of(Booking.Status.PENDING, Booking.Status.CONFIRMED);
        LocalDate tomorrow = LocalDate.now().plusDays(ONE_DAY);

        when(bookingRepository.findAllByCheckOutDateLessThanEqualAndStatusIn(tomorrow, statuses))
                .thenReturn(List.of(booking));

        bookingService.processExpiredBookings();

        assertEquals(Booking.Status.EXPIRED, booking.getStatus());
        verify(bookingRepository, times(1)).save(booking);

        verify(notificationService, times(1))
                .releasedAccommodation(eq(accommodationResponseDto),
                        any(UriComponentsBuilder.class));

        verify(notificationService, never()).notifyNoExpiredBookings();
    }
}
