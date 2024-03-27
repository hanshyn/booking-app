package com.booking.bookingapp.dto.accommodation;

import lombok.Data;

@Data
public class CreateAddressRequestDto {
    private String country;
    private String city;
    private String street;
    private int numberBuild;
    private String postcode;
}
