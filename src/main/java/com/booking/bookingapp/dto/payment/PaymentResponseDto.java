package com.booking.bookingapp.dto.payment;

import java.math.BigDecimal;

public record PaymentResponseDto(
        Long paymentId,
        Long bookingId,
        String status,
        BigDecimal amount,
        String paymentUrl
) {
}
