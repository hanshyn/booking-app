package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.user.UserResponseDto;
import com.booking.bookingapp.dto.user.UserUpdateRequestDto;
import com.booking.bookingapp.dto.user.UserUpdateRoleRequestDto;
import com.booking.bookingapp.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "Endpoints for managing users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Update user role",
            description = "Allows an admin to update the role of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User role updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}/role")
    public UserResponseDto updateRole(
            @Parameter(description = "ID of the user whose role is to be updated", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Data to update user role", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserUpdateRoleRequestDto.class)))
            @RequestBody @Valid UserUpdateRoleRequestDto requestDto) {
        return userService.updateRole(id, requestDto);
    }

    @Operation(summary = "Get current user",
            description = "Retrieves details of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User information retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/me")
    public UserResponseDto getUser(
            @Parameter(description = "Authentication object of the current user", hidden = true)
            Authentication authentication) {
        return userService.getUser(authentication);
    }

    @Operation(summary = "Update current user",
            description = "Updates details of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User information updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized access",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PutMapping("/me")
    public UserResponseDto updateUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated data for the current user", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserUpdateRequestDto.class)))
            @RequestBody @Valid UserUpdateRequestDto requestDto) {
        return userService.updateUser(requestDto);
    }
}
