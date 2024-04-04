package com.booking.bookingapp.dto.accommodation;

import com.booking.bookingapp.model.Accommodation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class CreateAccommodationRequestDto {
    @NotBlank
    private Accommodation.Types type;

    @NotBlank
    @Min(value = 1)
    private Long addressId;

    @NotBlank
    @Min(value = 1)
    private String size;

    @NotBlank
    @Min(value = 1)
    private List<Long> amenitiesId;

    @NotBlank
    @Min(value = 0)
    private BigDecimal dailyRate;

    @NotBlank
    @Min(value = 0)
    private int availability;
}
