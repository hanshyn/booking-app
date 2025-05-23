package com.booking.bookingapp.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.booking.bookingapp.dto.accommodation.AmenitiesResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAmenitiesRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/add-payments-and-bookings-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AmenitiesControllerTest {
    protected static MockMvc mockMvc;

    private static final Long VALID_ID = 1L;
    private static final String NAME = "shower";
    private static final String DESCRIPTION = "hot and cold water";
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
    @DisplayName("Create amenities")
    void createAmenities_ValidRequest_Success() throws Exception {
        CreateAmenitiesRequestDto createAmenitiesRequestDto = defoultCreateAmenitiesRequestDto();

        AmenitiesResponseDto expected
                = convertAmenitiesRequestToAmenitiesResponseDto(createAmenitiesRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/amenities").content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        AmenitiesResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), AmenitiesResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all amenities")
    void getAllAmenities_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/amenities")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<AmenitiesResponseDto> actual = List.of(objectMapper.readValue(
                result.getResponse().getContentAsString(), AmenitiesResponseDto[].class));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_TWO, actual.size());
    }

    @Test
    @DisplayName("Get amenities by id")
    void getAmenitiesById_ValidId_Success() throws Exception {
        AmenitiesResponseDto expected = defoultCreateAmenitiesResponseDto();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/amenities/{id}", VALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AmenitiesResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AmenitiesResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("update amenities by id")
    void updateAmenitiesById_ValidId_Success() throws Exception {
        CreateAmenitiesRequestDto updateAmenitiesRequestDto = defoultCreateAmenitiesRequestDto();
        AmenitiesResponseDto expected = convertAmenitiesRequestToAmenitiesResponseDto(
                updateAmenitiesRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(expected);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/amenities/{id}", VALID_ID)
                .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AmenitiesResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), AmenitiesResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("deleted amenities by id")
    void deletedAmenitiesById_ValidId_Success() throws Exception {
        MvcResult result = mockMvc.perform(delete("/amenities/{id}", VALID_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        Assertions.assertTrue(actual.isEmpty());
    }

    private CreateAmenitiesRequestDto defoultCreateAmenitiesRequestDto() {
        CreateAmenitiesRequestDto amenitiesRequestDto = new CreateAmenitiesRequestDto();
        amenitiesRequestDto.setName(NAME);
        amenitiesRequestDto.setDescription(DESCRIPTION);
        return amenitiesRequestDto;
    }

    private AmenitiesResponseDto convertAmenitiesRequestToAmenitiesResponseDto(
            CreateAmenitiesRequestDto createAmenitiesRequestDto) {
        AmenitiesResponseDto amenitiesResponseDto = new AmenitiesResponseDto();
        amenitiesResponseDto.setId(VALID_ID);
        amenitiesResponseDto.setName(createAmenitiesRequestDto.getName());
        amenitiesResponseDto.setDescription(createAmenitiesRequestDto.getDescription());
        return amenitiesResponseDto;
    }

    private AmenitiesResponseDto defoultCreateAmenitiesResponseDto() {
        AmenitiesResponseDto amenitiesResponseDto = new AmenitiesResponseDto();
        amenitiesResponseDto.setId(VALID_ID);
        amenitiesResponseDto.setName(NAME);
        amenitiesResponseDto.setDescription(DESCRIPTION);
        return amenitiesResponseDto;
    }
}
