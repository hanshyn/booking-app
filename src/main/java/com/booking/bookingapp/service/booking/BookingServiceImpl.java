package com.booking.bookingapp.service.booking;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.mapper.AddressMapper;
import com.booking.bookingapp.mapper.AmenitiesMapper;
import com.booking.bookingapp.mapper.BookingMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final AmenitiesRepository amenitiesRepository;
    private final AmenitiesMapper amenitiesMapper;

    @Transactional
    @Override
    public BookingResponseDto save(BookingRequestDto requestDto) {
        Booking booking = bookingMapper.toModel(requestDto);

        Accommodation accommodation = accommodationRepository.findById(
                requestDto.getAccommodationId()
        ).orElseThrow(() -> new RuntimeException("Can't found accommodation by id"
                + requestDto.getAccommodationId()));

        List<AmenitiesResponseDto> amenities = accommodation.getAmenities().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId.getId()).orElseThrow(
                        () -> new RuntimeException("Can't found amenities by id:" + amenitiesId)))
                .map(amenitiesMapper::toDto)
                .toList();

        User user = userRepository.findById(requestDto.getUserId()).orElseThrow(
                () -> new RuntimeException("Can't found user by id" + requestDto.getUserId())
        );

        booking.setAccommodation(accommodation);
        booking.setUser(user);

        bookingRepository.save(booking);

        BookingResponseDto bookingResponseDto = bookingMapper.toDto(booking);

        AccommodationResponseDto accommodationResponseDto
                = accommodationMapper.toDto(accommodation);

        accommodationResponseDto.setAmenities(amenities);

        Address address = addressRepository.findById(accommodation.getLocation().getId())
                .orElseThrow(
                        () -> new RuntimeException("Can't found address by id: "
                                + accommodation.getLocation().getId())
                );

        AddressResponseDto addressResponseDto = addressMapper.toDto(address);
        accommodationResponseDto.setLocation(addressResponseDto);
        bookingResponseDto.setAccommodation(accommodationResponseDto);

        return bookingResponseDto;
    }

    @Override
    public List<BookingResponseDto> search() {
        return null;
    }

    @Transactional
    @Override
    public List<BookingResponseDto> getByUser() {
        List<Booking> bookings = bookingRepository.findBookingsByUserId(1L);

        return bookings.stream().map(bookingMapper::toDto).toList();
    }

    @Transactional
    @Override
    public BookingResponseDto getById(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't found booking by id" + id)
        );

        return bookingMapper.toDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto updateById(Long id, BookingRequestDto requestDto) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't found booking by id" + id)
        );

        booking.setCheckInDate(requestDto.getCheckInDate());
        booking.setCheckOutDate(requestDto.getCheckOutDate());

        Accommodation accommodation
                = accommodationRepository.findById(requestDto.getAccommodationId()).orElseThrow();
        booking.setAccommodation(accommodation);
        User user = userRepository.findById(requestDto.getUserId()).orElseThrow();
        booking.setUser(user);
        booking.setStatus(requestDto.getStatus());

        bookingRepository.save(booking);

        return bookingMapper.toDto(booking);
    }

    @Override
    public void deleteById(Long id) {
        bookingRepository.deleteById(id);
    }
}
