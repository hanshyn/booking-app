package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import com.booking.bookingapp.service.notification.NotificationService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class AccommodationServiceImpl implements AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    private final AddressRepository addressRepository;
    private final AmenitiesRepository amenitiesRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public AccommodationResponseDto save(CreateAccommodationRequestDto requestDto,
                                         UriComponentsBuilder uriBuilder) {
        Accommodation accommodation = accommodationMapper.toModel(requestDto);
        accommodation.setType(requestDto.getType());
        accommodation.setLocation(getAddressById(requestDto.getAddressId()));
        accommodation.setAmenities(getAmenitiesByIds(requestDto.getAmenitiesId()));

        AccommodationResponseDto responseDto =
                accommodationMapper.toDto(accommodationRepository.save(accommodation));

        notificationService.createdAccommodation(responseDto, uriBuilder);
        return responseDto;
    }

    @Override
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationRepository.findAll(pageable).stream()
                .map(accommodationMapper::toDto)
                .toList();
    }

    @Override
    public AccommodationResponseDto getById(Long id) {
        return accommodationMapper.toDto(getAccommodationById(id));
    }

    @Transactional
    @Override
    public AccommodationResponseDto updateById(CreateAccommodationRequestDto requestDto, Long id) {
        Accommodation accommodation = getAccommodationById(id);

        accommodation.setType(requestDto.getType());
        accommodation.setLocation(getAddressById(requestDto.getAddressId()));
        accommodation.setSize(requestDto.getSize());
        accommodation.setAmenities(getAmenitiesByIds(requestDto.getAmenitiesId()));
        accommodation.setDailyRate(requestDto.getDailyRate());
        accommodation.setAvailability(requestDto.getAvailability());

        return accommodationMapper.toDto(accommodationRepository.save(accommodation));
    }

    @Override
    public void deleteById(Long id) {
        accommodationRepository.deleteById(id);
    }

    private Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found address gy id: " + id));
    }

    private Accommodation getAccommodationById(Long id) {
        return accommodationRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found accommodation by id: " + id));
    }

    private Set<Amenities> getAmenitiesByIds(List<Long> amenitiesIds) {
        return amenitiesIds.stream()
                .map(amenitiesId -> amenitiesRepository.findById(amenitiesId).orElseThrow(
                        () -> new EntityNotFoundException("Can't find amenities by id:"
                                + amenitiesId))).collect(Collectors.toSet());
    }
}
