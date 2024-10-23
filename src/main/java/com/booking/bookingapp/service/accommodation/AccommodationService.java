package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface AccommodationService {
    AccommodationResponseDto save(CreateAccommodationRequestDto requestDto,
                                  UriComponentsBuilder urlBuilder);

    List<AccommodationResponseDto> getAll(Pageable pageable);

    AccommodationResponseDto getById(Long ig);

    AccommodationResponseDto updateById(CreateAccommodationRequestDto requestDto, Long id);

    void deleteById(Long id);
}
