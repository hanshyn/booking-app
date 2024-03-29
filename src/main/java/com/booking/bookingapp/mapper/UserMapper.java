package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    User toModel(UserRegistrationRequestDto requestDto);

    UserResponseDto toDto(User user);
}
