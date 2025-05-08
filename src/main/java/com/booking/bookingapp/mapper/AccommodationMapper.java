package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.model.Accommodation;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class, uses = {AddressMapper.class, AmenitiesMapper.class})
public interface AccommodationMapper {
    Accommodation toModel(CreateAccommodationRequestDto requestDto);

    @Mapping(target = "location", source = "location")
    @Mapping(target = "amenities", source = "amenities")
    AccommodationResponseDto toDto(Accommodation accommodation);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CreateAccommodationRequestDto dto,
                       @MappingTarget Accommodation accommodation);
}
