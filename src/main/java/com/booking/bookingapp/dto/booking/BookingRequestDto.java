package com.booking.bookingapp.dto.booking;

import com.booking.bookingapp.model.Booking;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingRequestDto {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Long accommodationId;
    private Long userId;
    private Booking.Status status;
}
