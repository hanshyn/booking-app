package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import com.booking.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.service.booking.BookingService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Bookings", description = "Endpoints for managing bookings")
@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Operation(summary = "Create a new booking",
            description = "Creates a new booking with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created successfully",
                    content = @Content(
                            schema = @Schema(implementation = BookingResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking request data")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingResponseDto create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Booking request details", required = true,
                    content = @Content(schema = @Schema(implementation = BookingRequestDto.class)))
            @RequestBody @Valid BookingRequestDto requestDto, UriComponentsBuilder uriBuilder,
            @AuthenticationPrincipal User user) {
        return bookingService.save(requestDto, uriBuilder, user);
    }

    @Operation(summary = "Search bookings",
            description = "Searches bookings based on the provided search parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "403",
                    description = "Access forbidden for unauthorized users")
    })
    @PreAuthorize("hasAnyRole('MANAGER', 'ROLE_ADMIN')")
    @GetMapping()
    public List<BookingResponseDto> search(
            @Parameter(description = "Search parameters for booking", required = false)
            BookingSearchParameters searchParameters, Pageable pageable) {
        return bookingService.search(searchParameters, pageable);
    }

    @Operation(summary = "Get bookings for the current user",
            description = "Retrieves a list of bookings for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bookings retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/my")
    public List<BookingResponseDto> getByUser(Pageable pageable) {
        return bookingService.getByUser(pageable);
    }

    @Operation(summary = "Get booking by ID",
            description = "Retrieves details of a specific booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Booking details retrieved successfully",
                    content = @Content(
                            schema = @Schema(implementation = BookingResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @GetMapping("/{id}")
    public BookingResponseDto getById(
            @Parameter(description = "ID of the booking to retrieve", required = true)
            @PathVariable Long id) {
        return bookingService.getById(id);
    }

    @Operation(summary = "Update booking by ID",
            description = "Updates details of a specific booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated successfully",
                    content = @Content(
                            schema = @Schema(implementation = BookingResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid booking update data"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @PutMapping("/{id}")
    public BookingResponseDto updateById(
            @Parameter(description = "ID of the booking to update", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated booking details", required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateBookingRequestDto.class)))
            @RequestBody @Valid UpdateBookingRequestDto requestDto,
            UriComponentsBuilder uriBuilder) {
        return bookingService.updateById(id, requestDto, uriBuilder);
    }

    @Operation(summary = "Delete booking by ID",
            description = "Deletes a specific booking by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Booking deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @Parameter(description = "ID of the booking to delete", required = true)
            @PathVariable Long id) {
        bookingService.deleteById(id);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
