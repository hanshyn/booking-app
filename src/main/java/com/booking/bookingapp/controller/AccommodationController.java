package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.service.AccommodationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accommodations")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @PostMapping
    public AccommodationResponseDto createAccommodation(
            @RequestBody CreateAccommodationRequestDto requestDto) {
        return accommodationService.createAccommodation(requestDto);
    }

    @GetMapping
    public List<AccommodationResponseDto> getAll() {
        return accommodationService.getAll();
    }

    @GetMapping("/{id}")
    public AccommodationResponseDto getById(@PathVariable Long id) {
        return accommodationService.getById(id);
    }

    @PutMapping("/{id}")
    public AccommodationResponseDto updateById(
            @RequestBody CreateAccommodationRequestDto requestDto, @PathVariable Long id) {
        return accommodationService.updateById(requestDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        accommodationService.deleteById(id);
    }
}
