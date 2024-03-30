package com.booking.bookingapp.dto.user;

import com.booking.bookingapp.model.Role;
import java.util.Set;

public record UserResponseDto(
        Long id,
        String email,
        String firstName,
        String lastName,
        Set<Role> role
) {
}
