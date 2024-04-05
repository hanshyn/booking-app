package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface AddressService {
    AddressResponseDto save(CreateAddressRequestDto requestDto);

    List<AddressResponseDto> getAll(Pageable pageable);

    AddressResponseDto getById(Long id);

    AddressResponseDto updateById(CreateAddressRequestDto requestDto, Long id);

    void deleteById(Long id);
}
