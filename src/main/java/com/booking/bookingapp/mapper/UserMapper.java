package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.user.RoleResponseDto;
import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.model.User;
import java.util.Set;
import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(target = "password",
            expression = "java(passwordEncoder.encode(requestDto.password()))")
    User toModel(UserRegistrationRequestDto requestDto, @Context PasswordEncoder passwordEncoder);

    UserResponseDto toDto(User user);

    @Mapping(target = "role", source = "roleDtos")
    UserResponseDto toDto(User user, Set<RoleResponseDto> roleDtos);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(UserUpdateRequestDto requestDto, @MappingTarget User user);
}
