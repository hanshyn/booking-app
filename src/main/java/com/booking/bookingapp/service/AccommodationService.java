package com.booking.bookingapp.service;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;

public interface AccommodationService {
    AccommodationResponseDto createAccommodation(CreateAccommodationRequestDto requestDto);
}
