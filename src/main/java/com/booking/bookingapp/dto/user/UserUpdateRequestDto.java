package com.booking.bookingapp.dto.user;

public record UserUpdateRequestDto(
        String firstName,
        String lastName,
        String password,
        String repeatPassword,
        Long roleId
) {
}
