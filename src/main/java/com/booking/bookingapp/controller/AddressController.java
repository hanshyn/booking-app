package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import com.booking.bookingapp.service.accommodation.AddressService;
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
@RequestMapping(value = "address")
public class AddressController {
    private final AddressService addressService;

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AddressResponseDto create(@RequestBody @Valid CreateAddressRequestDto requestDto,
                                     UriComponentsBuilder builder) {
        return addressService.save(requestDto);
    }

    @GetMapping
    public List<AddressResponseDto> getAll(Pageable pageable) {
        return addressService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public AddressResponseDto getById(@PathVariable Long id) {
        return addressService.getById(id);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @PutMapping("/{id}")
    public AddressResponseDto updateById(@RequestBody @Valid CreateAddressRequestDto requestDto,
                                         @PathVariable Long id) {
        return addressService.updateById(requestDto, id);
    }

    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        addressService.deleteById(id);
    }
}
