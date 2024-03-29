package com.booking.bookingapp.service;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.mapper.AmenitiesMapper;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.AmenitiesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AmenitiesServiceImpl implements AmenitiesService {
    private final AmenitiesMapper amenitiesMapper;
    private final AmenitiesRepository amenitiesRepository;

    @Override
    public AmenitiesResponseDto save(CreateAmenitiesRequestDto requestDto) {
        return amenitiesMapper.toDto(
                amenitiesRepository.save(
                        amenitiesMapper.toModel(requestDto)
                )
        );
    }

    @Override
    public List<AmenitiesResponseDto> getAll() {
        return amenitiesRepository.findAll().stream()
                .map(amenitiesMapper::toDto)
                .toList();
    }

    @Override
    public AmenitiesResponseDto getById(Long id) {
        Amenities amenities = amenitiesRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find amenities by id: " + id)
        );

        return amenitiesMapper.toDto(amenities);
    }

    @Override
    public AmenitiesResponseDto updateById(CreateAmenitiesRequestDto requestDto, Long id) {
        Amenities amenities = amenitiesRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Can't find amenities by id: " + id)
        );

        amenities.setName(requestDto.getName());
        amenities.setDescription(requestDto.getDescription());

        return amenitiesMapper.toDto(amenitiesRepository.save(amenities));
    }

    @Override
    public void deleteById(Long id) {
        amenitiesRepository.deleteById(id);
    }
}
