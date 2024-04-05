package com.booking.bookingapp.dto.booking;

import com.booking.bookingapp.model.Booking;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;

@Data
public class BookingRequestDto {
    @FutureOrPresent
    private LocalDate checkInDate;
    @Future
    private LocalDate checkOutDate;
    @Min(value = 1, message = "AccommodationId should be greater than 1")
    private Long accommodationId;
    @Min(value = 1, message = "User id should be greater than 2")
    private Long userId;
    @Pattern(regexp = "PENDING|CONFIRMED|CANCELED|EXPIRED",
            message = "Status should be PENDING, CONFIRMED, CANCELED or EXPIRED")
    private Booking.Status status;
}
