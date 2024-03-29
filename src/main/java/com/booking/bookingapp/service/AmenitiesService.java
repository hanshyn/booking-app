package com.booking.bookingapp.service;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import java.util.List;

public interface AmenitiesService {
    AmenitiesResponseDto save(CreateAmenitiesRequestDto requestDto);

    List<AmenitiesResponseDto> getAll();

    AmenitiesResponseDto getById(Long id);

    AmenitiesResponseDto updateById(CreateAmenitiesRequestDto requestDto, Long id);

    void deleteById(Long id);
}
