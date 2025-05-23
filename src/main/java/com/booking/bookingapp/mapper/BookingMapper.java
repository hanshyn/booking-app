package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class, uses = {AccommodationMapper.class})
public interface BookingMapper {
    Booking toModel(BookingRequestDto requestDto);

    @Mapping(target = "accommodation", source = "accommodation")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "status", expression = "java(Booking.Status.PENDING)")
    @Mapping(target = "id", ignore = true)
    Booking toModel(BookingRequestDto dto, Accommodation accommodation, User user);

    @Mapping(target = "accommodation", source = "accommodation")
    BookingResponseDto toDto(Booking booking);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateModel(UpdateBookingRequestDto requestDto, @MappingTarget Booking booking);
}
