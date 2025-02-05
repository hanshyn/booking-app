package com.booking.bookingapp.service.accommodation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AmenitiesMapper;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
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
class AmenitiesServiceTest {
    private static final String NAME = "Wi-Fi";
    private static final String DESCRIPTION = "2.4 GHz, 5GHz";
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final String UPDATE_STRING = "update ";
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;

    @Mock
    private AmenitiesMapper amenitiesMapper;

    @Mock
    private AmenitiesRepository amenitiesRepository;

    @InjectMocks
    private AmenitiesServiceImpl amenitiesService;

    @Test
    @DisplayName("Create amenities with valid data. Send Creat")
    void save_ValidCreateAmenitiesRequestDto_ReturnValidAmenities() {
        CreateAmenitiesRequestDto amenitiesRequestDto = defaultCreateAmenitiesRequestDto();
        Amenities amenities = toModel(amenitiesRequestDto);
        amenities.setId(VALID_ID);

        AmenitiesResponseDto amenitiesResponseDto = toDto(amenities);

        when(amenitiesMapper.toModel(amenitiesRequestDto)).thenReturn(amenities);
        when(amenitiesRepository.save(amenities)).thenReturn(amenities);
        when(amenitiesMapper.toDto(amenities)).thenReturn(amenitiesResponseDto);

        AmenitiesResponseDto actual = amenitiesService.save(amenitiesRequestDto);

        Assertions.assertEquals(amenitiesResponseDto, actual);
    }

    @Test
    @DisplayName("Get all amenities - should return the complete list of amenities")
    void getAll_ValidPageable_ReturnAllAmenities() {
        Amenities amenities = defaultAmenities();
        amenities.setId(VALID_ID);

        AmenitiesResponseDto amenitiesResponseDto = toDto(amenities);

        Pageable pageable = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
        List<Amenities> amenitiesList = List.of(amenities);
        Page<Amenities> amenitiesPage =
                new PageImpl<>(amenitiesList, pageable, amenitiesList.size());

        when(amenitiesRepository.findAll(pageable)).thenReturn(amenitiesPage);
        when(amenitiesMapper.toDto(amenities)).thenReturn(amenitiesResponseDto);

        List<AmenitiesResponseDto> actual = amenitiesService.getAll(pageable);

        Assertions.assertEquals(amenitiesList.size(), actual.size());
    }

    @Test
    @DisplayName("Get amenities by valid id, return AmenitiesResponseDto")
    void getById_ValidAmenitiesId_ReturnValidAmenitiesDto() {
        Amenities amenities = new Amenities();
        amenities.setId(VALID_ID);

        AmenitiesResponseDto amenitiesResponseDto = toDto(amenities);

        when(amenitiesRepository.findById(VALID_ID)).thenReturn(Optional.of(amenities));
        when(amenitiesMapper.toDto(amenities)).thenReturn(amenitiesResponseDto);

        AmenitiesResponseDto actual = amenitiesService.getById(VALID_ID);

        Assertions.assertEquals(amenities.getId(), actual.getId());
    }

    @Test
    @DisplayName("Get amenities by invalid id, return EntityNotFoundException")
    void getById_InvalidAmenitiesId_NotOk() {
        when(amenitiesRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> amenitiesService.getById(INVALID_ID));
    }

    @Test
    @DisplayName("Update amenities by id, return AmenitiesResponseDto")
    void updateById_ValidAmenitiesId_ReturnAmenitiesResponseDto() {
        Amenities amenities = defaultAmenities();
        amenities.setId(VALID_ID);

        CreateAmenitiesRequestDto updateAmenitiesDto = updateAmenitiesRequestDto();

        Amenities updateAmenities = defaultAmenities();
        updateAmenities.setId(VALID_ID);
        updateAmenities.setName(updateAmenitiesDto.getName());
        updateAmenities.setDescription(updateAmenitiesDto.getDescription());

        AmenitiesResponseDto amenitiesResponseDto = toDto(updateAmenities);

        when(amenitiesRepository.findById(VALID_ID)).thenReturn(Optional.of(amenities));
        when(amenitiesRepository.save(any(Amenities.class))).thenReturn(updateAmenities);
        when(amenitiesMapper.toDto(updateAmenities)).thenReturn(amenitiesResponseDto);

        AmenitiesResponseDto actual = amenitiesService.updateById(updateAmenitiesDto, VALID_ID);

        Assertions.assertEquals(VALID_ID, actual.getId());
        Assertions.assertEquals(updateAmenitiesDto.getName(), actual.getName());
        Assertions.assertEquals(updateAmenities.getDescription(), actual.getDescription());
    }

    @Test
    @DisplayName("Update amenities by invalid id, return EntityNotFoundException")
    void updateById_InvalidAmenitiesId_NotOk() {
        CreateAmenitiesRequestDto updateAmenities = new CreateAmenitiesRequestDto();

        when(amenitiesRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> amenitiesService.updateById(updateAmenities, INVALID_ID));
    }

    @Test
    @DisplayName("Should delete amenities by valid ID by calling deleteById once in the repository")
    void deleteById_ValidAmenitiesId() {
        amenitiesService.deleteById(VALID_ID);
        verify(amenitiesRepository, times(1)).deleteById(VALID_ID);
    }

    private Amenities defaultAmenities() {
        Amenities amenities = new Amenities();
        amenities.setName(NAME);
        amenities.setDescription(DESCRIPTION);
        return amenities;
    }

    private CreateAmenitiesRequestDto defaultCreateAmenitiesRequestDto() {
        CreateAmenitiesRequestDto amenitiesRequestDto = new CreateAmenitiesRequestDto();
        amenitiesRequestDto.setName(NAME);
        amenitiesRequestDto.setDescription(DESCRIPTION);
        return amenitiesRequestDto;
    }

    private CreateAmenitiesRequestDto updateAmenitiesRequestDto() {
        CreateAmenitiesRequestDto updateAmenitiesDto = new CreateAmenitiesRequestDto();
        updateAmenitiesDto.setName(NAME + UPDATE_STRING);
        updateAmenitiesDto.setDescription(DESCRIPTION + UPDATE_STRING);
        return updateAmenitiesDto;
    }

    private AmenitiesResponseDto toDto(Amenities amenities) {
        AmenitiesResponseDto amenitiesResponseDto = new AmenitiesResponseDto();
        amenitiesResponseDto.setId(amenities.getId());
        amenitiesResponseDto.setName(amenities.getName());
        amenitiesResponseDto.setDescription(amenities.getDescription());
        return amenitiesResponseDto;
    }

    private Amenities toModel(CreateAmenitiesRequestDto requestDto) {
        Amenities amenities = new Amenities();
        amenities.setName(requestDto.getName());
        amenities.setDescription(requestDto.getDescription());
        return amenities;
    }
}
