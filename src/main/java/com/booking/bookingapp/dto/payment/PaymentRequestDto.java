package com.booking.bookingapp.dto.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PaymentRequestDto(
        @NotNull
        @Positive
        Long bookingId
) {
}
