package com.booking.bookingapp.dto.booking;

public record BookingSearchParameters(
        String[] userId,
        String[] status
) {
}
