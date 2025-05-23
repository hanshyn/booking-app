package com.booking.bookingapp.dto.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserUpdateRequestDto(
        @NotBlank
        @Length(min = 4, max = 20)
        String firstName,
        @NotBlank
        @Length(min = 4, max = 20)
        String lastName
) {
}
