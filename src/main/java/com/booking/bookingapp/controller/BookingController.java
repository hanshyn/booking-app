package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import com.booking.bookingapp.dto.booking.UpdateBookingRequestDto;
import com.booking.bookingapp.service.booking.BookingService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public BookingResponseDto create(@RequestBody @Valid BookingRequestDto requestDto,
                                     UriComponentsBuilder uriBuilder) {
        return bookingService.save(requestDto, uriBuilder);
    }

    @PreAuthorize("hasAnyRole('MANAGER', 'ROLE_ADMIN')")
    @GetMapping()
    public List<BookingResponseDto> search(BookingSearchParameters searchParameters,
                                           Pageable pageable) {
        return bookingService.search(searchParameters, pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/my")
    public List<BookingResponseDto> getByUser(Pageable pageable) {
        return bookingService.getByUser(pageable);
    }

    @GetMapping("/{id}")
    public BookingResponseDto getById(@PathVariable Long id) {
        return bookingService.getById(id);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @PutMapping("/{id}")
    public BookingResponseDto updateById(@PathVariable Long id,
                                         @RequestBody @Valid UpdateBookingRequestDto requestDto,
                                         UriComponentsBuilder uriBuilder) {
        return bookingService.updateById(id, requestDto, uriBuilder);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_USER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookingService.deleteById(id);
    }
}
