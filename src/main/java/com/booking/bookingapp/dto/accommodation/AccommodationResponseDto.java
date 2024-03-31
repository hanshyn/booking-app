package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationResponseDto {
    private Long id;
    private Accommodation.Types type;
    private AddressResponseDto location;
    private String size;
    private List<AmenitiesResponseDto> amenities;
    private BigDecimal dailyRate;
    private int availability;
}
