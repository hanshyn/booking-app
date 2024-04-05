package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AmenitiesService {
    AmenitiesResponseDto save(CreateAmenitiesRequestDto requestDto);

    List<AmenitiesResponseDto> getAll(Pageable pageable);

    AmenitiesResponseDto getById(Long id);

    AmenitiesResponseDto updateById(CreateAmenitiesRequestDto requestDto, Long id);

    void deleteById(Long id);
}
