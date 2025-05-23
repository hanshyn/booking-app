package com.booking.bookingapp.dto.accommodation;

import lombok.Data;

@Data
public class AddressResponseDto {
    private Long id;
    private String country;
    private String city;
    private String street;
    private int numberBuild;
    private String postcode;
}
