package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import com.booking.bookingapp.service.accommodation.AddressService;
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
@RequestMapping(value = "address")
public class AddressController {
    private final AddressService addressService;

    @PostMapping
    public AddressResponseDto create(@RequestBody CreateAddressRequestDto requestDto) {
        return addressService.save(requestDto);
    }

    @GetMapping
    public List<AddressResponseDto> getAll() {
        return addressService.getAll();
    }

    @GetMapping("/{id}")
    public AddressResponseDto getById(@PathVariable Long id) {
        return addressService.getById(id);
    }

    @PutMapping("/{id}")
    public AddressResponseDto updateById(@RequestBody CreateAddressRequestDto requestDto,
                                         @PathVariable Long id) {
        return addressService.updateById(requestDto, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        addressService.deleteById(id);
    }
}
