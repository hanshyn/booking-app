package com.booking.bookingapp.dto.user;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateRoleRequestDto(
        @NotBlank
        @Min(value = 2)
        Long roleId
) {
}
