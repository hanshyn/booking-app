package com.booking.bookingapp.service.user;

import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.exception.RegistrationException;
import org.springframework.security.core.Authentication;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    UserResponseDto updateRole(Long id, UserUpdateRoleRequestDto requestDto);

    UserResponseDto getUser(Authentication authentication);

    UserResponseDto updateUser(UserUpdateRequestDto requestDto);
}
