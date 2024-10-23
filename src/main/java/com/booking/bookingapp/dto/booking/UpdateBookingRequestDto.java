package com.booking.bookingapp.dto.booking;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.validation.EnumNamePattern;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UpdateBookingRequestDto {
    @FutureOrPresent
    private LocalDate checkInDate;

    @Future
    private LocalDate checkOutDate;

    @NotNull
    @Min(value = 1, message = "AccommodationId should be greater than 1")
    private Long accommodationId;

    @EnumNamePattern(regexp = "PENDING|CONFIRMED|PAID|CANCELED|EXPIRED",
            message = "Status should be PENDING, CONFIRMED, PAID, CANCELED or EXPIRED")
    private Booking.Status status;
}
