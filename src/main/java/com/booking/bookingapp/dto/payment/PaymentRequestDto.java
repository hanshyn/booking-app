package com.booking.bookingapp.dto.payment;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(
        @NotNull
        @Min(value = 1)
        Long bookingId
) {
}
