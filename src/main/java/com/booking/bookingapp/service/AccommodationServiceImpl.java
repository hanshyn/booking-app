package com.booking.bookingapp.service;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.AccommodationRepository;
import com.booking.bookingapp.repository.AddressRepository;
import com.booking.bookingapp.repository.AmenitiesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AmenitiesRepository amenitiesRepository;

    @Override
    public AccommodationResponseDto createAccommodation(CreateAccommodationRequestDto requestDto) {
        Address address = addressRepository.findById(requestDto.getAddressId()).orElseThrow(
                () -> new RuntimeException("Can't found address by address_id:"
                        + requestDto.getAddressId())
        );

        List<Amenities> amenities = requestDto.getAmenitiesId().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new RuntimeException("Can't found amenities by id: " + amenitiesId)
                ))
                .toList();

        System.out.println(amenities);

        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setLocation(address);
        accommodation.setAmenities(amenities);
        accommodationRepository.save(accommodation);
        System.out.println(amenities.get(0));
        return accommodationMapper.toDto(accommodation);
    }

    private List<Amenities> getAmenitiesByIds(CreateAccommodationRequestDto requestDto) {
        return requestDto.getAmenitiesId().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new RuntimeException("Can't find category by id:"
                                + amenitiesId)))
                .toList();
    }
}
