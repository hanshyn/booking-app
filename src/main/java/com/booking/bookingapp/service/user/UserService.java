package com.booking.bookingapp.service.user;

import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto);
}
