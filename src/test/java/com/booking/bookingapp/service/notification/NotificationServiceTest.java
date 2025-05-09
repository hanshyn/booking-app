package com.booking.bookingapp.service.notification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.accommodation.AddressResponseDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.model.Accommodation;
import com.booking.bookingapp.model.Address;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.Role;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import com.booking.bookingapp.telegram.TelegramBotBookingApp;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    private static final Long VALID_ID = 1L;
    private static final Long INVALID_ID = 100L;
    private static final Long VALID_TELEGRAM_ID = 1234L;
    private static final String VALID_LINK_BOOKINGS = "www.booking.com/booking/bookings/1";
    private static final String VALID_LINK_ACCOMMODATIONS
            = "www.booking.com/booking/accommodations/1";
    private static final String STATUS_PAYMENT = "PAY";
    private final UriComponentsBuilder uriBuilder
            = UriComponentsBuilder.fromUriString("www.booking.com/booking");

    @Mock
    private TelegramBotBookingApp telegramBotBookingApp;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private BookingResponseDto bookingResponseDto;
    private AccommodationResponseDto accommodationResponseDto;
    private PaymentSuccessDto paymentSuccessDto;
    private Booking booking;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field urlField = NotificationServiceImpl.class.getDeclaredField("url");
        urlField.setAccessible(true);
        urlField.set(notificationService, uriBuilder.toString());

        bookingResponseDto = new BookingResponseDto();
        bookingResponseDto.setId(VALID_ID);

        accommodationResponseDto = new AccommodationResponseDto();
        accommodationResponseDto.setId(VALID_ID);
        accommodationResponseDto.setLocation(new AddressResponseDto());
        accommodationResponseDto.setType(Accommodation.Type.APARTMENT);

        bookingResponseDto.setAccommodation(accommodationResponseDto);

        booking = new Booking();
        booking.setId(VALID_ID);
        booking.setStatus(Booking.Status.PENDING);
        Accommodation accommodation = new Accommodation();
        accommodation.setLocation(new Address());
        booking.setAccommodation(accommodation);

        User adminUser = new User();
        adminUser.setTelegramId(VALID_TELEGRAM_ID);

        paymentSuccessDto = new PaymentSuccessDto(
                VALID_ID,
                bookingResponseDto.getId(),
                STATUS_PAYMENT, BigDecimal.valueOf(10L));

        when(userRepository.findAllByRoles_Role(Role.RoleName.ADMIN))
                .thenReturn(List.of(adminUser));
    }

    @Test
    @DisplayName("Send notification when a new booking is created")
    void createdBooking_SendNotification() {
        notificationService.createdBooking(bookingResponseDto, uriBuilder);

        verify(telegramBotBookingApp, times(1)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                eq(VALID_LINK_BOOKINGS)
        );

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString()
        );
    }

    @Test
    @DisplayName("Send notification when booking is canceled")
    void canceledBooking_SendNotification() {
        notificationService.canceledBooking(
                VALID_TELEGRAM_ID,
                bookingResponseDto,
                uriBuilder,
                bookingResponseDto.getStatus()
        );

        verify(telegramBotBookingApp, times(1)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                eq(VALID_LINK_BOOKINGS)
        );

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString()
        );
    }

    @Test
    @DisplayName("Send notification when new accommodation is created")
    void createdAccommodation_SendNotification() {
        notificationService.createdAccommodation(accommodationResponseDto, uriBuilder);

        verify(telegramBotBookingApp, times(1)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                eq(VALID_LINK_ACCOMMODATIONS)
        );

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString()
        );
    }

    @Test
    @DisplayName("Send notification when accommodation is released")
    void releasedAccommodation_SendNotification() {
        notificationService.releasedAccommodation(accommodationResponseDto, uriBuilder);

        verify(telegramBotBookingApp, times(1)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                eq(VALID_LINK_ACCOMMODATIONS)
        );

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString()
        );
    }

    @Test
    @DisplayName("Send notification about payment status")
    void notifyAboutPayment_SendNotification() {
        when(bookingRepository.findById(VALID_ID)).thenReturn(Optional.of(booking));

        notificationService.notifyAboutPayment(paymentSuccessDto);

        verify(telegramBotBookingApp, times(1)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                anyString()
        );

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString()
        );
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when booking ID is invalid")
    void notifyAboutPayment_InvalidBookingId_ShouldThrowException() {
        bookingResponseDto.setId(INVALID_ID);
        PaymentSuccessDto paymentSuccessDto = new PaymentSuccessDto(
                VALID_ID,
                bookingResponseDto.getId(),
                STATUS_PAYMENT,
                BigDecimal.valueOf(10L));

        when(bookingRepository.findById(paymentSuccessDto.bookingId()))
                .thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> notificationService.notifyAboutPayment(paymentSuccessDto));

        assertEquals("Can't found booking by id: " + paymentSuccessDto.bookingId(),
                exception.getMessage());
    }

    @Test
    @DisplayName("Send notification when on expired bookings found ")
    void notifyNoExpiredBookings_SendNotification() {
        notificationService.notifyNoExpiredBookings();
        String message = "No expired bookings today!";

        verify(telegramBotBookingApp, times(1))
                .sendNotification(eq(VALID_TELEGRAM_ID), eq(message));

        verify(telegramBotBookingApp, times(0)).sendNotification(
                eq(VALID_TELEGRAM_ID),
                anyString(),
                anyString());
    }
}
