package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AmenitiesMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AmenitiesServiceImpl implements AmenitiesService {
    private final AmenitiesMapper amenitiesMapper;
    private final AmenitiesRepository amenitiesRepository;
    private final AccommodationRepository accommodationRepository;

    @Transactional
    @Override
    public AmenitiesResponseDto save(CreateAmenitiesRequestDto requestDto) {
        return amenitiesMapper.toDto(
                amenitiesRepository.save(
                        amenitiesMapper.toModel(requestDto)
                )
        );
    }

    @Override
    public List<AmenitiesResponseDto> getAll(Pageable pageable) {
        return amenitiesRepository.findAll(pageable).stream()
                .map(amenitiesMapper::toDto)
                .toList();
    }

    @Override
    public AmenitiesResponseDto getById(Long id) {
        return amenitiesMapper.toDto(getAmenitiesById(id));
    }

    @Transactional
    @Override
    public AmenitiesResponseDto updateById(CreateAmenitiesRequestDto requestDto, Long id) {
        Amenities amenities = getAmenitiesById(id);

        amenitiesMapper.updateFromDto(requestDto, amenities);

        return amenitiesMapper.toDto(amenitiesRepository.save(amenities));
    }

    @Override
    public void deleteById(Long id) {
        Set<Accommodation> accommodations = accommodationRepository
                .findAccommodationByAmenities_Id(id);

        for (Accommodation accommodation : accommodations) {
            accommodation.getAmenities().removeIf(a -> a.getId().equals(id));
            accommodationRepository.save(accommodation);
        }
        amenitiesRepository.deleteById(id);
    }

    private Amenities getAmenitiesById(Long id) {
        return amenitiesRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found amenities by id: " + id));
    }
}
