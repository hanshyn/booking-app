package com.booking.bookingapp.dto.user;

public record UserRegistrationRequestDto(
        String email,
        String firstName,
        String lastName,
        String password,
        String repeatPassword
) {
}
