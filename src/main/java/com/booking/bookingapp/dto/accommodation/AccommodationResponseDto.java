package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class AccommodationResponseDto {
    private Long id;
    private Accommodation.Types type;
    private Address location;
    private String size;
    private List<Amenities> amenities;
    private BigDecimal dailyRate;
    private int availability;
}
