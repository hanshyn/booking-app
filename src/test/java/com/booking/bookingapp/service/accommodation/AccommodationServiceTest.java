package com.booking.bookingapp.service.accommodation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.mapper.AccommodationMapper;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Amenities;
import com.booking.bookingapp.repository.accommodation.AccommodationRepository;
import com.booking.bookingapp.repository.accommodation.AddressRepository;
import com.booking.bookingapp.repository.accommodation.AmenitiesRepository;
import com.booking.bookingapp.service.notification.NotificationService;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class AccommodationServiceTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;

    @Mock
    private EntityManager entityManager;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private AccommodationMapper accommodationMapper;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AmenitiesRepository amenitiesRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private AccommodationServiceImpl accommodationService;

    private Accommodation accommodation;
    private Address address;
    private Amenities amenities;
    private AccommodationResponseDto accommodationResponseDto;
    private CreateAccommodationRequestDto accommodationRequestDto;
    private UriComponentsBuilder uriBuilder;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(VALID_ID);

        amenities = new Amenities();
        amenities.setId(VALID_ID);

        accommodation = new Accommodation();
        accommodation.setId(VALID_ID);
        accommodation.setLocation(address);
        accommodation.setAmenities(Set.of(amenities));
        accommodation.setType(Accommodation.Types.APARTMENT);

        accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(VALID_ID);

        accommodationRequestDto = new CreateAccommodationRequestDto();
        accommodationRequestDto.setAddressId(VALID_ID);
        accommodationRequestDto.setAmenitiesId(List.of(VALID_ID));
        accommodationRequestDto.setType(Accommodation.Types.APARTMENT);

        uriBuilder = UriComponentsBuilder.newInstance();

        lenient().when(addressRepository.findById(VALID_ID)).thenReturn(Optional.of(address));
        lenient().when(amenitiesRepository.findById(VALID_ID)).thenReturn(Optional.of(amenities));
        lenient().when(accommodationMapper.toModel(accommodationRequestDto))
                .thenReturn(accommodation);
        lenient().when(accommodationMapper.toDto(accommodation))
                .thenReturn(accommodationResponseDto);
    }

    @Test
    @DisplayName("Create accommodation with valid data")
    void save_ValidCreateAccommodationRequestDto_ReturnValidAccommodation() {
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);

        AccommodationResponseDto actual
                = accommodationService.save(accommodationRequestDto, uriBuilder);

        assertEquals(accommodationResponseDto, actual);
        verify(accommodationRepository, times(1)).save(accommodation);
        verify(notificationService, times(1))
                .createdAccommodation(accommodationResponseDto, uriBuilder);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when address ID is invalid")
    void getAddressById_InvalidId_ThrowsException() {
        accommodationRequestDto.setAddressId(INVALID_ID);
        when(addressRepository.findById(accommodationRequestDto.getAddressId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.save(accommodationRequestDto, uriBuilder));

        assertEquals("Can't found address gy id: "
                + accommodationRequestDto.getAddressId(), exception.getMessage());
    }

    @Test
    @DisplayName("Get all accommodations with pagination")
    void getAll_ReturnsPageAccommodations() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Accommodation> accommodations = List.of(accommodation);
        Page<Accommodation> accommodationPage = new PageImpl<>(accommodations);

        when(accommodationRepository.findAll(pageable)).thenReturn(accommodationPage);

        List<AccommodationResponseDto> actual = accommodationService.getAll(pageable);

        assertEquals(1, actual.size());
        verify(accommodationRepository, times(1)).findAll(pageable);
        verify(accommodationMapper, times(1)).toDto(accommodation);
    }

    @Test
    @DisplayName("Get accommodation by valid id")
    void getById_ValidAccommodationID_ReturnAccommodation() {
        when(accommodationRepository.findById(VALID_ID)).thenReturn(Optional.of(accommodation));

        AccommodationResponseDto actual = accommodationService.getById(VALID_ID);

        assertEquals(accommodationResponseDto, actual);
        verify(accommodationRepository, times(1)).findById(VALID_ID);
        verify(accommodationMapper, times(1)).toDto(accommodation);
    }

    @Test
    @DisplayName("Get accommodation by invalid id, return EntityNotFoundException")
    void getById_InvalidAccommodationId_NotOk() {
        when(accommodationRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accommodationService.getById(INVALID_ID));
    }

    @Test
    @DisplayName("")
    void getAmenitiesById_InvalidId_ThrowsException() {
        accommodationRequestDto.setAmenitiesId(List.of(INVALID_ID));
        when(amenitiesRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accommodationService.save(accommodationRequestDto, uriBuilder));
        assertEquals("Can't find amenities by id:" + INVALID_ID, exception.getMessage());
    }

    @Test
    @DisplayName("Update accommodation by valid id")
    void updateById_ValidAccommodationId_ReturnAccommodation() {
        when(accommodationRepository.findById(VALID_ID)).thenReturn(Optional.of(accommodation));
        when(accommodationRepository.save(accommodation)).thenReturn(accommodation);

        AccommodationResponseDto actual
                = accommodationService.updateById(accommodationRequestDto, VALID_ID);

        assertEquals(accommodationResponseDto, actual);
        verify(accommodationRepository, times(1)).save(accommodation);
    }

    @Test
    @DisplayName("Update accommodation by invalid id, return EntityNotFoundException")
    void updateById_InvalidAccommodationId_NotOk() {
        when(accommodationRepository.findById(INVALID_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> accommodationService.updateById(accommodationRequestDto, INVALID_ID));
    }

    @Test
    @DisplayName("Delete accommodation by valid id")
    void deleteById_ValidAccommodationId() {
        accommodationService.deleteById(VALID_ID);

        verify(accommodationRepository, times(1)).deleteById(VALID_ID);
    }
}
