package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.model.Amenities;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AmenitiesMapper {
    Amenities toModel(CreateAmenitiesRequestDto requestDto);

    AmenitiesResponseDto toDto(Amenities amenities);
}
