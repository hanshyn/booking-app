package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.user.UserLoginRequestDto;
import com.booking.bookingapp.dto.user.UserLoginResponseDto;
import com.booking.bookingapp.dto.user.UserRegistrationRequestDto;
import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.exception.RegistrationException;
import com.booking.bookingapp.security.AuthenticationService;
import com.booking.bookingapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @Operation(summary = "Register a new user",
            description = "Creates a new user account in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid registration data",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserResponseDto register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration data for creating a new user", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserRegistrationRequestDto.class)))
            @RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        log.info("Registering new user with email: {}", requestDto.email());
        return userService.register(requestDto);
    }

    @Operation(summary = "User login",
            description = "Authenticates a user and returns an access token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(
                            schema = @Schema(implementation = UserLoginResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid login credentials",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/login")
    public UserLoginResponseDto login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials for authentication", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserLoginRequestDto.class)))
            @RequestBody @Valid UserLoginRequestDto requestDto) {
        log.info("Login credentials for authentication: {}", requestDto.email());
        return authenticationService.authenticate(requestDto);
    }
}
