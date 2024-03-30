package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id,
                                      @RequestBody UserUpdateRoleRequestDto requestDto) {
        return userService.updateRole(requestDto);
    }

    @GetMapping("/me")
    public UserResponseDto userInfo() {
        return null;
    }

    @PutMapping("/me")
    public UserResponseDto updateUser(@RequestBody UserUpdateRequestDto requestDto) {
        return userService.updateUser(requestDto);
    }
}
