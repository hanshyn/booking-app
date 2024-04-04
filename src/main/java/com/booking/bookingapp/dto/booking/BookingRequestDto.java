package com.booking.bookingapp.dto.booking;

import com.booking.bookingapp.model.Booking;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class BookingRequestDto {
    @NotBlank
    @Length(min = 4, max = 20)
    @FutureOrPresent
    private LocalDate checkInDate;
    @NotBlank
    @Length(min = 4, max = 20)
    @Future
    private LocalDate checkOutDate;
    @NotBlank
    private Long accommodationId;
    @NotBlank
    private Long userId;
    @NotBlank
    @Length(min = 4, max = 20)
    private Booking.Status status;
}
