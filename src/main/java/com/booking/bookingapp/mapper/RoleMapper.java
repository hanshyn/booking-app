package com.booking.bookingapp.mapper;

import com.booking.bookingapp.config.MapperConfig;
import com.booking.bookingapp.dto.user.RoleResponseDto;
import com.booking.bookingapp.model.Role;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface RoleMapper {
    RoleResponseDto toDto(Role role);
}
