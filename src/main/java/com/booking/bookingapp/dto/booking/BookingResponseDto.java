package com.booking.bookingapp.dto.booking;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.model.Booking;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingResponseDto {
    private Long id;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private AccommodationResponseDto accommodation;
    private Booking.Status status;
}
