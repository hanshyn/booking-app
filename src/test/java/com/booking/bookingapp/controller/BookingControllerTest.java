package com.booking.bookingapp.controller;

import static com.booking.bookingapp.model.Accommodation.Types.HOUSE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.booking.BookingRequestDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.model.Booking;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/add-payments-and-bookings-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingControllerTest {
    protected static MockMvc mockMvc;

    private static final String SEARCH_PARAM_NAME_USER_ID = "id";
    private static final String SEARCH_USER_ID = "10";
    private static final String SEARCH_PARAM_STATUS = "status";
    private static final String SEARCH_STATUS = "PAID";
    private static final Long VALID_BOOKING_ID = 1L;
    private static final Long VALID_ACCOMMODATION_ID = 1L;
    private static final Long ONE_DAY = 1L;
    private static final LocalDate NOW_DATE = LocalDate.now();
    private static final LocalDate DATE_ONE_DAY = LocalDate.now().plusDays(ONE_DAY);
    private static final String SIZE_APARTMENT = "Studio";
    private static final int EXPECTED_ONE = 1;
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
    @WithUserDetails("admin@gmail.com")
    @DisplayName("Search booking with param user_id and status")
    public void searchWithOneParam_ReturnBookingResponseDtos() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add(SEARCH_PARAM_NAME_USER_ID, SEARCH_USER_ID);
        params.add(SEARCH_PARAM_STATUS, SEARCH_STATUS);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookings")
                .params(params))
                .andExpect(status().isOk())
                .andReturn();

        List<BookingResponseDto> actual = List.of(objectMapper
                .readValue(result.getResponse().getContentAsString(), BookingResponseDto[].class));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_ONE, actual.size());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    @DisplayName("Search booking with param user_id")
    public void searchWithTwoParam_ReturnBookingResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("/bookings")
                        .param(SEARCH_PARAM_NAME_USER_ID, SEARCH_USER_ID))
                .andExpect(status().isOk())
                .andReturn();

        List<BookingResponseDto> actual = List.of(objectMapper
                .readValue(result.getResponse().getContentAsString(), BookingResponseDto[].class));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_TWO, actual.size());
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    @DisplayName("Create new booking")
    public void creatBooking_ValidRequest_Success() throws Exception {
        BookingRequestDto bookingRequestDto = defaultBookingRequestDto();

        BookingResponseDto expected
                = convertBookingRequestDtoToBookingResponseDto(bookingRequestDto);

        String jsonRequest = objectMapper.writeValueAsString(bookingRequestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        BookingResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), BookingResponseDto.class);

        EqualsBuilder.reflectionEquals(expected, actual,"id");
    }

    @Test
    @WithUserDetails("admin@gmail.com")
    @DisplayName("Return booing by user")
    public void getByUSer_ReturnBookingResponseDtosByUser() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookings/my")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookingResponseDto> actual = List.of(
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        BookingResponseDto[].class));

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(EXPECTED_TWO, actual.size());
    }

    /*@Test
    @DisplayName("Return booking by id")
    public void getBookingById_ValidRequest_Success() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .get("/bookings/{id}", VALID_BOOKING_ID)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookingResponseDto actual
                = objectMapper.readValue(result.getResponse().getContentAsString(),
                BookingResponseDto.class);

        Assertions.assertNotNull(actual);
        Assertions.assertEquals(VALID_BOOKING_ID, actual.getId());
        Assertions.assertEquals(HOUSE, actual.getAccommodation().getType());
        Assertions.assertEquals(SIZE_APARTMENT, actual.getAccommodation().getSize());
    }*/

    private BookingRequestDto defaultBookingRequestDto() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        bookingRequestDto.setAccommodationId(VALID_ACCOMMODATION_ID);
        bookingRequestDto.setCheckInDate(NOW_DATE);
        bookingRequestDto.setCheckOutDate(DATE_ONE_DAY);
        return bookingRequestDto;
    }

    private BookingResponseDto convertBookingRequestDtoToBookingResponseDto(
            BookingRequestDto requestDto) {
        BookingResponseDto responseDto = new BookingResponseDto();
        responseDto.setAccommodation(defaultAccommodationResponseDto());
        responseDto.setCheckInDate(requestDto.getCheckInDate());
        responseDto.setCheckOutDate(requestDto.getCheckOutDate());
        responseDto.setStatus(Booking.Status.PENDING);
        return responseDto;
    }

    private BookingResponseDto defaultBookingResponseDto() {
        BookingResponseDto bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(VALID_BOOKING_ID);
        bookingResponseDto.setAccommodation(new AccommodationResponseDto());
        bookingResponseDto.setCheckInDate(NOW_DATE);
        bookingResponseDto.setCheckOutDate(DATE_ONE_DAY);
        bookingResponseDto.setStatus(Booking.Status.COMPLETED);
        return bookingResponseDto;
    }

    private AccommodationResponseDto defaultAccommodationResponseDto() {
        AccommodationResponseDto responseDto = new AccommodationResponseDto();
        responseDto.setId(VALID_ACCOMMODATION_ID);
        responseDto.setType(HOUSE);
        responseDto.setSize(SIZE_APARTMENT);
        return responseDto;
    }
}
