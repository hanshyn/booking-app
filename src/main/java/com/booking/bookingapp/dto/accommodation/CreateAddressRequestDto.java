package com.booking.bookingapp.dto.accommodation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAddressRequestDto {
    @NotBlank
    @Size(min = 4, max = 20)

    @NotBlank
    @Size(min = 4, max = 20)
    private String country;

    @NotBlank
    @Size(min = 4, max = 20)
    private String city;

    @NotBlank
    @Size(min = 4, max = 20)
    private String street;

    @NotBlank
    @Size(min = 4, max = 20)
    private int numberBuild;

    @NotBlank
    @Size(min = 6, max = 8)
    private String postcode;
}
