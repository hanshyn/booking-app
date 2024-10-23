package com.booking.bookingapp.dto.accommodation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CreateAmenitiesRequestDto {

    @NotBlank
    @Length(min = 3, max = 20)
    private String name;

    @NotBlank
    @Length(min = 3, max = 20)
    private String description;
}
