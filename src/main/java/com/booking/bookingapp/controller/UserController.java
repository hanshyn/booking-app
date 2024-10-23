package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(@PathVariable Long id,
                                      @RequestBody @Valid UserUpdateRoleRequestDto requestDto) {
        return userService.updateRole(id, requestDto);
    }

    @GetMapping("/me")
    public UserResponseDto getUser(Authentication authentication) {
        return userService.getUser(authentication);
    }

    @PutMapping("/me")
    public UserResponseDto updateUser(@RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.updateUser(requestDto);
    }
}
