package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AccommodationService {
    AccommodationResponseDto createAccommodation(CreateAccommodationRequestDto requestDto);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto getById(Long ig);

    AccommodationResponseDto updateById(CreateAccommodationRequestDto requestDto, Long id);

    void deleteById(Long id);
}
