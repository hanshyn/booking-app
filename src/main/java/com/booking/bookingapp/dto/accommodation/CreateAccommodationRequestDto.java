package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Data
public class CreateAccommodationRequestDto {
    @Pattern(regexp = "HOUSE|APARTMENT|CONDO|VACATION_HOME",
            message = "Type should be HOUSE, APARTMENT, CONDO or VACATION_HOME")
    private Accommodation.Types type;
    @Min(value = 1)
    private Long addressId;
    @NotBlank
    private String size;
    @UniqueElements(message = "AmenitiesId should be unique")
    private List<Long> amenitiesId;
    @Min(value = 0)
    private BigDecimal dailyRate;
    @Min(value = 0)
    private int availability;
}
