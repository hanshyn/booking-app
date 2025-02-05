package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.service.accommodation.AccommodationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@Tag(name = "Accommodation", description = "Endpoints for managing accommodations")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @Operation(summary = "Create accommodation", description = "Creates new accommodation entity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Accommodation created successfully",
                    content = @Content(
                            schema = @Schema(implementation = AccommodationResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto create(
            @Parameter(description = "Data for creating a new accommodation", required = true)
            @RequestBody @Valid CreateAccommodationRequestDto requestDto,
            UriComponentsBuilder urlBuilder) {
        return accommodationService.save(requestDto, urlBuilder);
    }

    @Operation(summary = "Get all accommodations",
            description = "Retrieves a paginated list of all accommodations")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AccommodationResponseDto.class)))
    })
    @GetMapping
    public List<AccommodationResponseDto> getAll(
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @Operation(summary = "Get accommodation by ID",
            description = "Retrieve a single accommodation by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Accommodation retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AccommodationResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Accommodation not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public AccommodationResponseDto getById(
            @Parameter(description = "ID of the accommodation to retrieve", required = true)
            @PathVariable Long id) {
        return accommodationService.getById(id);
    }

    @Operation(summary = "Update accommodation",
            description = "Updates an existing accommodation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Accommodation updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = AccommodationResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Accommodation not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @PutMapping("/{id}")
    public AccommodationResponseDto updateById(
            @Parameter(description = "Data for updating the accommodation", required = true)
            @RequestBody @Valid CreateAccommodationRequestDto requestDto,
            @Parameter(description = "ID of the accommodation to update", required = true)
            @PathVariable Long id) {
        return accommodationService.updateById(requestDto, id);
    }

    @Operation(summary = "Delete accommodation", description = "Deletes an accommodation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Accommodation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Accommodation not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "ID of the accommodation to delete", required = true)
            @PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
