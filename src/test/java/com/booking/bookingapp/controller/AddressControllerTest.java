package com.booking.bookingapp.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.accommodation.CreateAddressRequestDto;
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
import org.springframework.security.test.context.support.WithUserDetails;
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
public class AddressControllerTest {
    protected static MockMvc mockMvc;

    private static final int EXPECTED_TWO = 2;
    private static final String COUNTRY = "Ukraine";
    private static final String CITY = "Kyiv";
    private static final String STREET = "Shevchenko";
    private static final String POSTCODE = "01011";
    private static final Integer BUILD_NUMBER = 1;
    private static final Long VALID_ID = 1L;

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
    @DisplayName("Create new address")
    public void createAddress_ValidRequest_Success() throws Exception {
        CreateAddressRequestDto createAddressRequestDto = defaultCreateAddressRequestDto();

        AddressResponseDto expected
                = convertAddressRequestDtoToAddressResponseDto(createAddressRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(createAddressRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/addresses")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        AddressResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), AddressResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all addresses")
    public void getAllAddresses_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/addresses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<AddressResponseDto> actual = List.of(objectMapper
                .readValue(result.getResponse().getContentAsString(), AddressResponseDto[].class)
        );

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_TWO, actual.size());
    }

    @Test
    @DisplayName("Get address by id")
    public void getAddressById_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/addresses/{id}", VALID_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AddressResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), AddressResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_ID, actual.getId());
        Assertions.assertEquals(COUNTRY, actual.getCountry());
        Assertions.assertEquals(CITY, actual.getCity());
        Assertions.assertEquals(STREET, actual.getStreet());
        Assertions.assertEquals(POSTCODE, actual.getPostcode());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    @DisplayName("Update address by id")
    public void updateAddressById_ValidRequest_Success() throws Exception {
        CreateAddressRequestDto updateAddressRequestDto = defaultCreateAddressRequestDto();

        AddressResponseDto expected = convertAddressRequestDtoToAddressResponseDto(
                updateAddressRequestDto);
        String jsonRequest = objectMapper.writeValueAsString(updateAddressRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/addresses/{id}", VALID_ID)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        AddressResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), AddressResponseDto.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual);
    }

    private CreateAddressRequestDto defaultCreateAddressRequestDto() {
        CreateAddressRequestDto createAddressRequestDto = new CreateAddressRequestDto();
        createAddressRequestDto.setCountry(COUNTRY);
        createAddressRequestDto.setCity(CITY);
        createAddressRequestDto.setStreet(STREET);
        createAddressRequestDto.setPostcode(POSTCODE);
        createAddressRequestDto.setNumberBuild(BUILD_NUMBER);
        return createAddressRequestDto;
    }

    private AddressResponseDto convertAddressRequestDtoToAddressResponseDto(
            CreateAddressRequestDto createAddressRequestDto) {
        AddressResponseDto addressResponseDto = new AddressResponseDto();
        addressResponseDto.setStreet(createAddressRequestDto.getStreet());
        addressResponseDto.setCity(createAddressRequestDto.getCity());
        addressResponseDto.setCountry(createAddressRequestDto.getCountry());
        addressResponseDto.setPostcode(createAddressRequestDto.getPostcode());
        addressResponseDto.setNumberBuild(createAddressRequestDto.getNumberBuild());
        return addressResponseDto;
    }
}
