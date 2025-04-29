package com.booking.bookingapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAccommodationRequestDto;
import com.booking.bookingapp.model.Accommodation;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/add-payments-and-bookings-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AccommodationControllerTest {
    protected static MockMvc mockMvc;

    private static final Long ADDRESS_ID = 1L;
    private static final Long ACCOMMODATION_ID = 1L;
    private static final int AVAILABILITY = 10;
    private static final BigDecimal DALY_RATE = BigDecimal.valueOf(10.5);
    private static final String SIZE = "Studio";
    private static final int EXPECTED_TWO = 2;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create accommodation")
    void createAccommodation_ValidRequest_Success() throws Exception {
        CreateAccommodationRequestDto requestDto = defaultedAccommodationRequestDto();

        AccommodationResponseDto expected
                = convertCreateAccommodationDtoToAccommodationResponseDto(requestDto);

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post("/accommodations").content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        AccommodationResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationResponseDto.class
        );

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all accommodation")
    void getAllAccommodation_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/accommodations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<AccommodationResponseDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationResponseDto[].class));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_TWO, actual.size());
    }

    @Test
    @Transactional
    @DisplayName("Get accommodation by id")
    void getAccommodationById_ValidRequest_Success() throws Exception {
        AccommodationResponseDto expected = defaultedAccommodationResponseDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/accommodations/{id}", ACCOMMODATION_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AccommodationResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Update accommodation by id")
    void updateAccommodationById_ValidRequest_Success() throws Exception {
        CreateAccommodationRequestDto updateRequestDto = defaultedAccommodationRequestDto();

        AccommodationResponseDto expected
                = convertCreateAccommodationDtoToAccommodationResponseDto(updateRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(updateRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .put("/accommodations/{id}", ACCOMMODATION_ID)
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AccommodationResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AccommodationResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Deleted accommodation by id")
    void deleteAccommodationById_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(
                delete("/accommodations/{id}", ACCOMMODATION_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        Assertions.assertTrue(actual.isEmpty());
    }

    private CreateAccommodationRequestDto defaultedAccommodationRequestDto() {
        CreateAccommodationRequestDto requestDto = new CreateAccommodationRequestDto();
        requestDto.setAddressId(ADDRESS_ID);
        requestDto.setType(Accommodation.Types.HOUSE);
        requestDto.setAmenitiesId(List.of());
        requestDto.setAvailability(AVAILABILITY);
        requestDto.setDailyRate(DALY_RATE);
        requestDto.setSize(SIZE);
        return requestDto;
    }

    private AccommodationResponseDto convertCreateAccommodationDtoToAccommodationResponseDto(
            CreateAccommodationRequestDto createAccommodationRequestDto) {
        AccommodationResponseDto accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(ACCOMMODATION_ID);
        accommodationResponseDto.setSize(createAccommodationRequestDto.getSize());
        accommodationResponseDto.setType(createAccommodationRequestDto.getType());
        accommodationResponseDto.setAmenities(List.of());
        accommodationResponseDto.setLocation(new AddressResponseDto());
        accommodationResponseDto.setAvailability(createAccommodationRequestDto.getAvailability());
        accommodationResponseDto.setDailyRate(createAccommodationRequestDto.getDailyRate());
        return accommodationResponseDto;
    }

    private AccommodationResponseDto defaultedAccommodationResponseDto() {
        AccommodationResponseDto responseDto = new AccommodationResponseDto();
        responseDto.setId(ACCOMMODATION_ID);
        responseDto.setType(Accommodation.Types.HOUSE);
        responseDto.setAmenities(List.of());
        responseDto.setLocation(new AddressResponseDto());
        responseDto.setAvailability(AVAILABILITY);
        responseDto.setDailyRate(DALY_RATE);
        return responseDto;
    }
}
