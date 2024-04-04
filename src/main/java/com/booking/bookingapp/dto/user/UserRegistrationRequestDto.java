package com.booking.bookingapp.dto.user;

import com.booking.bookingapp.validation.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@FieldMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "Password not match")
public record UserRegistrationRequestDto(
        @NotBlank
        @Length(min = 8, max = 20)
        @Email
        String email,
        @NotBlank
        @Length(min = 8, max = 20)
        String firstName,
        @NotBlank
        @Length(min = 8, max = 20)
        String lastName,
        @NotBlank
        @Length(min = 8, max = 20)
        String password,
        @NotBlank
        @Length(min = 8, max = 20)
        String repeatPassword,
        @NotBlank
        @Min(1)
        Long roleId
) {
}
