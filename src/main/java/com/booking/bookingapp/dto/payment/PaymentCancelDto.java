package com.booking.bookingapp.dto.payment;

public record PaymentCancelDto(
        String paymentUrl,
        String message
) {
}
