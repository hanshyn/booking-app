package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateAccommodationRequestDto {
    private Accommodation.Types type;
    private Long addressId;
    private String size;
    private List<Long> amenitiesId;
    private BigDecimal dailyRate;
    private int availability;
}
