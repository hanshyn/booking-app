package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import java.util.List;

public interface AddressService {
    AddressResponseDto save(CreateAddressRequestDto requestDto);

    List<AddressResponseDto> getAll();

    AddressResponseDto getById(Long id);

    AddressResponseDto updateById(CreateAddressRequestDto requestDto, Long id);

    void deleteById(Long id);
}
