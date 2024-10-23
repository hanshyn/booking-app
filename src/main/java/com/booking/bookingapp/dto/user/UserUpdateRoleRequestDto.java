package com.booking.bookingapp.dto.user;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRoleRequestDto(
        @NotNull
        @Min(value = 2, message = "Role id should be greater than 2")
        @Max(value = 4, message = "Role id should be less than 4")
        Long roleId
) {
}
