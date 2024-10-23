package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.service.accommodation.AccommodationService;
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
@RequestMapping(value = "/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AccommodationResponseDto create(
            @RequestBody @Valid CreateAccommodationRequestDto requestDto,
            UriComponentsBuilder urlBuilder) {
        return accommodationService.save(requestDto, urlBuilder);
    }

    @GetMapping
    public List<AccommodationResponseDto> getAll(Pageable pageable) {
        return accommodationService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.getById(id);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @PutMapping("/{id}")
    public AccommodationResponseDto updateById(
            @RequestBody @Valid CreateAccommodationRequestDto requestDto, @PathVariable Long id) {
        return accommodationService.updateById(requestDto, id);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
