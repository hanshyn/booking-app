package com.booking.bookingapp.dto.user;

public record UserLoginRequestDto(
        String email,
        String password
) {
}
