package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.service.accommodation.AmenitiesService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
@RequestMapping(value = "/amenities")
public class AmenitiesController {
    private final AmenitiesService amenitiesService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    private AmenitiesResponseDto create(
            @RequestBody @Valid CreateAmenitiesRequestDto requestDto) {
        return amenitiesService.save(requestDto);
    }

    @GetMapping
    public List<AmenitiesResponseDto> getAll(Pageable pageable) {
        return amenitiesService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AmenitiesResponseDto getById(@PathVariable Long id) {
        return amenitiesService.getById(id);
    }

    @PutMapping("/{id}")
    public AmenitiesResponseDto updateById(@RequestBody @Valid CreateAmenitiesRequestDto requestDto,
                                           @PathVariable Long id) {
        return amenitiesService.updateById(requestDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        amenitiesService.deleteById(id);
    }
}
