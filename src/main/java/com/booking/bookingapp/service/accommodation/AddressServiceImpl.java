package com.booking.bookingapp.service.accommodation;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AddressMapper;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements AddressService {
    private final AddressMapper addressMapper;
    private final AddressRepository addressRepository;

    @Transactional
    @Override
    public AddressResponseDto save(CreateAddressRequestDto requestDto) {
        return addressMapper.toDto(
                addressRepository.save(
                        addressMapper.toModel(requestDto)
                )
        );
    }

    @Override
    public List<AddressResponseDto> getAll(Pageable pageable) {
        return addressRepository.findAll(pageable).stream()
                .map(addressMapper::toDto)
                .toList();
    }

    @Override
    public AddressResponseDto getById(Long id) {
        return addressMapper.toDto(getAddressById(id));
    }

    @Transactional
    @Override
    public AddressResponseDto updateById(CreateAddressRequestDto requestDto, Long id) {
        Address address = getAddressById(id);
        address.setCountry(requestDto.getCountry());
        address.setCity(requestDto.getCity());
        address.setStreet(requestDto.getStreet());
        address.setNumberBuild(requestDto.getNumberBuild());
        address.setPostcode(requestDto.getPostcode());

        return addressMapper.toDto(addressRepository.save(address));
    }

    @Override
    public void deleteById(Long id) {
        addressRepository.deleteById(id);
    }

    private Address getAddressById(Long id) {
        return addressRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Can't found address by id: " + id));
    }
}
