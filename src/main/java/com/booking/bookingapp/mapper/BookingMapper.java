package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.model.Booking;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookingMapper {
    Booking toModel(BookingRequestDto requestDto);

    BookingResponseDto toDto(Booking booking);
}
