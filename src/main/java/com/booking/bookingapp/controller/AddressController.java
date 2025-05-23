package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import com.booking.bookingapp.service.accommodation.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Address", description = "Endpoint for managing addresses")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "addresses")
public class AddressController {
    private final AddressService addressService;

    @Operation(summary = "Create address", description = "Creates new address entity")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Address created successfully",
                    content = @Content(
                            schema = @Schema(implementation = AddressResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AddressResponseDto create(
            @Parameter(description = "Data for creating a new address", required = true)
            @RequestBody @Valid CreateAddressRequestDto requestDto) {
        log.info("Create address: {}", requestDto);
        return addressService.save(requestDto);
    }

    @Operation(summary = "Get all addresses",
            description = "Retrieves a paginated list of all addresses")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "List retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AddressResponseDto.class)))
    })
    @GetMapping
    public List<AddressResponseDto> getAll(
            @Parameter(description = "Pagination information")
            Pageable pageable) {
        log.debug("Get all addresses: {}", pageable);
        return addressService.getAll(pageable);
    }

    @Operation(summary = "Get address by ID",
            description = "Retrieve a single address by its ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Address retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = AddressResponseDto.class))),
            @ApiResponse(responseCode = "404",
                    description = "Address not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public AddressResponseDto getById(
            @Parameter(description = "ID of the address to retrieve", required = true)
            @PathVariable Long id) {
        log.debug("Get address by ID: {}", id);
        return addressService.getById(id);
    }

    @Operation(summary = "Update address",
            description = "Updates an existing address by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Address updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = AddressResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @PutMapping("/{id}")
    public AddressResponseDto updateById(
             @Parameter(description = "Data for updating the address", required = true)
             @RequestBody @Valid CreateAddressRequestDto requestDto,
             @Parameter(description = "ID of the address to update", required = true)
             @PathVariable Long id) {
        log.info("Update address ID: {} with data: {}", id, requestDto);
        return addressService.updateById(requestDto, id);
    }

    @Operation(summary = "Delete address", description = "Deletes an address by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "403", description = "Forbidden access")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(
            @Parameter(description = "ID of the address to delete", required = true)
            @PathVariable Long id) {
        log.info("Delete address by ID: {}", id);
        addressService.deleteById(id);
    }
}
