package com.booking.bookingapp.dto.accommodation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAddressRequestDto {
    @NotBlank
    @Size(min = 3, max = 30)
    private String country;

    @NotBlank
    @Size(min = 3, max = 30)
    private String city;

    @NotBlank
    @Size(min = 3, max = 30)
    private String street;

    @NotNull
    @Min(value = 1)
    @Max(value = 5000)
    private int numberBuild;

    @NotBlank
    @Size(min = 5, max = 10)
    private String postcode;
}
