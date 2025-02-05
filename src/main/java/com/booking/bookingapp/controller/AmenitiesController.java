package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.service.accommodation.AmenitiesService;
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

@Tag(name = "Amenities", description = "Endpoint for managing amenities")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/amenities")
public class AmenitiesController {
    private final AmenitiesService amenitiesService;

    @Operation(summary = "Create amenities", description = "Creates new amenities entity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "amenities created successfully",
                    content = @Content(
                            schema = @Schema(implementation = AmenitiesResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AmenitiesResponseDto create(
            @Parameter(description = "Data for creating a new amenities", required = true)
            @RequestBody @Valid CreateAmenitiesRequestDto requestDto) {
        return amenitiesService.save(requestDto);
    }

    @Operation(summary = "Get all amenities",
            description = "Retrieves a paginated list of all amenities")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AmenitiesResponseDto.class)))
    })
    @GetMapping
    public List<AmenitiesResponseDto> getAll(
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        return amenitiesService.getAll(pageable);
    }

    @Operation(summary = "Get amenities by ID",
            description = "Retrieve a single amenities by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Amenities retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AmenitiesResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Address not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public AmenitiesResponseDto getById(
            @Parameter(description = "ID of the amenities to retrieve", required = true)
            @PathVariable Long id) {
        return amenitiesService.getById(id);
    }

    @Operation(summary = "Update amenities",
            description = "Updates an existing amenities by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Amenities updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = AmenitiesResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Amenities not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @PutMapping("/{id}")
    public AmenitiesResponseDto updateById(
            @Parameter(description = "Data for updating the amenities", required = true)
            @RequestBody @Valid CreateAmenitiesRequestDto requestDto,
            @Parameter(description = "ID of the amenities to retrieve", required = true)
            @PathVariable Long id) {
        return amenitiesService.updateById(requestDto, id);
    }

    @Operation(summary = "Delete amenities", description = "Deletes an amenities by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Amenities deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Amenities not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "ID of the amenities to delete", required = true)
            @PathVariable Long id) {
        amenitiesService.deleteById(id);
    }
}
