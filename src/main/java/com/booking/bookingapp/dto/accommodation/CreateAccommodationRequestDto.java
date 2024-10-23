package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.validation.EnumNamePattern;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class CreateAccommodationRequestDto {

    @EnumNamePattern(
            regexp = "HOUSE|APARTMENT|CONDO|VACATION_HOME|HOTEL|HOSTEL|MOTEL|LUXURY_TENT|VILLA",
            message = "Type should be HOUSE, APARTMENT, CONDO, VACATION_HOME, HOTEL, HOSTEL, "
                    + "MOTEL, LUXURY_TENT or VILLA"
    )
    private Accommodation.Types type;

    @NotNull
    @Min(value = 1)
    private Long addressId;

    @NotBlank
    private String size;

    @UniqueElements(message = "AmenitiesId should be unique")
    private List<Long> amenitiesId;

    @NotNull
    @Min(value = 1)
    private BigDecimal dailyRate;

    @NotNull
    @Min(value = 1)
    private int availability;
}
