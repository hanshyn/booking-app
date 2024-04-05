package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AmenitiesRepository amenitiesRepository;

    @Transactional
    @Override
    public AccommodationResponseDto createAccommodation(CreateAccommodationRequestDto requestDto) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);

        Accommodation.Types types = requestDto.getType();

        Address address = addressRepository.findById(requestDto.getAddressId()).orElseThrow(
                () -> new RuntimeException("Can't found address by address_id:"
                        + requestDto.getAddressId())
        );

        List<Amenities> amenities = requestDto.getAmenitiesId().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new RuntimeException("Can't found amenities by id: " + amenitiesId)
                ))
                .toList();

        accommodation.setType(types);
        accommodation.setLocation(address);
        accommodation.setAmenities(amenities);

        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Transactional
    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public AccommodationResponseDto getById(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't found accommodation by id:" + id)
        );

        return accommodationMapper.toDto(accommodation);
    }

    @Transactional
    @Override
    public AccommodationResponseDto updateById(CreateAccommodationRequestDto requestDto, Long id) {
        Accommodation.Types type = requestDto.getType();

        Accommodation accommodation = accommodationRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't found accommodation by id:" + id)
        );

        Address address = addressRepository.findById(requestDto.getAddressId()).orElseThrow(
                () -> new RuntimeException(
                        "Can't found address by id: " + requestDto.getAddressId()
                )
        );

        List<Amenities> amenities = requestDto.getAmenitiesId().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new RuntimeException("Can't found amenities by id:" + amenitiesId)))
                .toList();

        accommodation.setType(type);
        accommodation.setLocation(address);
        accommodation.setSize(requestDto.getSize());
        accommodation.setAmenities(amenities);
        accommodation.setDailyRate(requestDto.getDailyRate());
        accommodation.setAvailability(requestDto.getAvailability());

        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.deleteById(id);
    }

    private List<Amenities> getAmenitiesByIds(CreateAccommodationRequestDto requestDto) {
        return requestDto.getAmenitiesId().stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new RuntimeException("Can't find category by id:"
                                + amenitiesId)))
                .toList();
    }
}
