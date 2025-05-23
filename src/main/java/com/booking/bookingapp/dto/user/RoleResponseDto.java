package com.booking.bookingapp.dto.user;

import com.booking.bookingapp.model.Role;

public record RoleResponseDto(
        Role.RoleName role
) {
}
