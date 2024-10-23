package com.booking.bookingapp.dto.payment;

import java.math.BigDecimal;

public record PaymentSuccessDto(
        Long paymentId,
        Long bookingId,
        String paymentStatus,
        BigDecimal amount
) {
}
