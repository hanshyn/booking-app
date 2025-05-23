package com.booking.bookingapp.dto.user;

import java.util.Set;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Set<RoleResponseDto> role
) {
}
