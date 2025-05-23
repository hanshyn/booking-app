package com.booking.bookingapp.service.accommodation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AddressMapper;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String COUNTRY = "Ukraine";
    private static final String CITY = "Kyiv";
    private static final String STREET = "Solomenska";
    private static final int NUMBER_BUILD = 1;
    private static final String POST_CODE = "01001";
    private static final String UPDATE_STRING = "new data string";
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @Test
    @DisplayName("Create address with valid data. Send Creat")
    public void save_ValidCreateAddressRequestDto_ReturnValidAddress() {
        CreateAddressRequestDto addressRequestDto = defaultAddressRequestDto();
        Address address = toModel(addressRequestDto);
        address.setId(VALID_ID);
        AddressResponseDto addressResponseDto = toDto(address);

        when(addressMapper.toModel(addressRequestDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);
        when(addressMapper.toDto(address)).thenReturn(addressResponseDto);

        AddressResponseDto actual = addressService.save(addressRequestDto);

        Assertions.assertEquals(addressResponseDto, actual);
    }

    @Test
    @DisplayName("Get all addresses - should return the complete list of addresses")
    public void getAll_ValidPageable_ReturnAllAddress() {
        Address address = defaultAddress();
        address.setId(VALID_ID);

        AddressResponseDto addressResponseDto = toDto(address);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Address> addressList = List.of(address);
        Page<Address> addressPage = new PageImpl<>(addressList, pageable, addressList.size());

        when(addressRepository.findAll(pageable)).thenReturn(addressPage);
        when(addressMapper.toDto(address)).thenReturn(addressResponseDto);

        List<AddressResponseDto> actual = addressService.getAll(pageable);

        Assertions.assertEquals(addressList.size(), actual.size());
    }

    @Test
    @DisplayName("Get address by valid id, return AddressResponseDto by id")
    public void getAddressById_ValidAddressId_ReturnValidAddressDto() {
        Address address = new Address();
        address.setId(VALID_ID);

        AddressResponseDto addressResponseDto = toDto(address);

        when(addressRepository.findById(VALID_ID)).thenReturn(Optional.of(address));
        when(addressMapper.toDto(address)).thenReturn(addressResponseDto);

        AddressResponseDto actual = addressService.getById(VALID_ID);

        Assertions.assertEquals(address.getId(), actual.getId());
    }

    @Test
    @DisplayName("Get address by invalid id, return EntityNotFoundEntityException")
    public void getAddressById_InvalidAddressId_NotOk() {
        when(addressRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> addressService.getById(INVALID_ID));
    }

    @Test
    @DisplayName("Update address by valid id, return AddressResponseDto by id")
    public void updateById_ValidAddressId_ReturnUpdateAddressDto() {
        Address address = defaultAddress();
        address.setId(VALID_ID);

        CreateAddressRequestDto updateAddressDto = updateAddressDto();

        Address updateAddress = defaultAddress();

        updateAddress.setId(VALID_ID);
        updateAddress.setCountry(updateAddressDto.getCountry());
        updateAddress.setCity(updateAddressDto.getStreet());
        updateAddress.setStreet(updateAddressDto.getStreet());
        updateAddress.setNumberBuild(updateAddressDto.getNumberBuild());
        updateAddress.setPostcode(updateAddressDto.getPostcode());

        AddressResponseDto updateAddressResponseDto = toDto(updateAddress);

        when(addressRepository.findById(VALID_ID)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(updateAddress);
        when(addressMapper.toDto(updateAddress)).thenReturn(updateAddressResponseDto);

        AddressResponseDto actual = addressService.updateById(updateAddressDto, VALID_ID);

        Assertions.assertEquals(VALID_ID, actual.getId());
        Assertions.assertEquals(updateAddressDto.getCountry(), actual.getCountry());
        Assertions.assertEquals(updateAddressDto.getStreet(), updateAddressResponseDto.getStreet());
    }

    @Test
    @DisplayName("Update address by invalid id, return EntityNotFoundEntityException")
    public void updateById_InvalidAddressId_NotOk() {
        CreateAddressRequestDto updateBookRequestDto = new CreateAddressRequestDto();

        when(addressRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> addressService.updateById(updateBookRequestDto, INVALID_ID));
    }

    @Test
    @DisplayName("Should delete address by valid ID by calling deleteById once in the repository")
    public void deleteById_validAddressId() {
        addressService.deleteById(VALID_ID);
        verify(addressRepository, times(1)).deleteById(VALID_ID);
    }

    private Address defaultAddress() {
        Address address = new Address();
        address.setCountry(COUNTRY);
        address.setCity(CITY);
        address.setStreet(STREET);
        address.setNumberBuild(NUMBER_BUILD);
        address.setPostcode(POST_CODE);
        return address;
    }

    private CreateAddressRequestDto defaultAddressRequestDto() {
        CreateAddressRequestDto addressRequestDto = new CreateAddressRequestDto();
        addressRequestDto.setCountry(COUNTRY);
        addressRequestDto.setCity(CITY);
        addressRequestDto.setStreet(STREET);
        addressRequestDto.setNumberBuild(NUMBER_BUILD);
        addressRequestDto.setPostcode(POST_CODE);
        return addressRequestDto;
    }

    private Address toModel(CreateAddressRequestDto addressRequestDto) {
        Address address = new Address();
        address.setCountry(addressRequestDto.getCountry());
        address.setCity(addressRequestDto.getStreet());
        address.setStreet(addressRequestDto.getStreet());
        address.setNumberBuild(addressRequestDto.getNumberBuild());
        address.setPostcode(addressRequestDto.getPostcode());
        return address;
    }

    private AddressResponseDto toDto(Address address) {
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setId(address.getId());
        addressResponseDto.setCountry(address.getCountry());
        addressResponseDto.setCity(address.getCity());
        addressResponseDto.setStreet(address.getStreet());
        addressResponseDto.setNumberBuild(address.getNumberBuild());
        addressResponseDto.setPostcode(address.getPostcode());
        return addressResponseDto;
    }

    private CreateAddressRequestDto updateAddressDto() {
        CreateAddressRequestDto updateAddressDto = new CreateAddressRequestDto();
        updateAddressDto.setCountry(UPDATE_STRING + COUNTRY);
        updateAddressDto.setCity(UPDATE_STRING + CITY);
        updateAddressDto.setStreet(UPDATE_STRING + STREET);
        updateAddressDto.setNumberBuild(NUMBER_BUILD + NUMBER_BUILD);
        updateAddressDto.setPostcode(UPDATE_STRING + POST_CODE);
        return updateAddressDto;
    }
}
