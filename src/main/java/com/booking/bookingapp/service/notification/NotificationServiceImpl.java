package com.booking.bookingapp.service.notification;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.Role.RoleName;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import com.booking.bookingapp.telegram.TelegramBotBookingApp;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {
    private static final String TYPE = "Type: ";
    private static final String SIZE = "Size: ";
    private static final String COUNTRY = "Country: ";
    private static final String CITY = "City: ";
    private static final String STREET = "Street: ";
    private static final String DAILY_RATE = "Daily rate: ";
    private static final String AVAILABILITY = "Availability: ";
    private static final String SEPARATION = "\n";
    private static final String CHECK_IN_DATA = "Check in data: ";
    private static final String CHECK_OUT_DATA = "Check out data: ";
    private static final String STATUS = "Status: ";
    private static final String CREATED = "Created";
    private static final String RELEASED = "Released";
    private static final String BOOKING_ID = "Booking id: ";
    private static final String PAYMENT_STATUS = "Payment status: ";
    private static final String AMOUNT_TOTAL = "Amount total: ";
    private static final String PATH_ACCOMMODATION = "accommodations";
    private static final String PATH_BOOKINGS = "bookings";
    private static final String EXPIRED_BOOKING = "No expired bookings today!";

    private final TelegramBotBookingApp telegramBotBookingApp;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    @Value("${site.url}")
    private String url;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Async
    @Transactional
    @Override
    public void createdBooking(BookingResponseDto responseDto,
                               UriComponentsBuilder urlBuilder) {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(),
                    buildBookingNotificationText(responseDto),
                    buildUrl(urlBuilder, PATH_BOOKINGS, responseDto.getId()));
        }
    }

    @Async
    @Override
    public void canceledBooking(Long telegramId, BookingResponseDto responseDto,
                                UriComponentsBuilder urlBuilder, Booking.Status status) {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(),
                    buildBookingNotificationText(responseDto),
                    buildUrl(urlBuilder, PATH_BOOKINGS, responseDto.getId()));
        }
    }

    @Async
    @Override
    public void createdAccommodation(AccommodationResponseDto responseDto,
                                     UriComponentsBuilder urlBuilder) {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(),
                    buildAccommodationNotificationText(responseDto, CREATED),
                    buildUrl(urlBuilder, PATH_ACCOMMODATION, responseDto.getId()));
        }
    }

    @Override
    public void releasedAccommodation(AccommodationResponseDto responseDto,
                                      UriComponentsBuilder urlBuilder) {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(),
                    buildAccommodationNotificationText(responseDto, RELEASED),
                    buildUrl(urlBuilder, PATH_ACCOMMODATION, responseDto.getId()));
        }
    }

    @Override
    public void notifyAboutPayment(PaymentSuccessDto paymentSuccessDto) {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(),
                    buildPaymentNotification(paymentSuccessDto),
                    buildUrl(
                            UriComponentsBuilder.fromUriString(url + contextPath),
                            PATH_BOOKINGS,
                            paymentSuccessDto.bookingId())
            );
        }
    }

    @Override
    public void notifyNoExpiredBookings() {
        List<User> recipients = getAdminsAndManagers();

        for (User user : recipients) {
            telegramBotBookingApp.sendNotification(user.getTelegramId(), EXPIRED_BOOKING);
        }
    }

    private List<User> getAdminsAndManagers() {
        List<User> admins = userRepository.findAllByRoles_Role(RoleName.ADMIN);
        List<User> managers = userRepository.findAllByRoles_Role(RoleName.MANAGER);

        List<User> recipients = new ArrayList<>();
        recipients.addAll(admins);
        recipients.addAll(managers);
        log.info("recipients users: {}", recipients);
        return recipients;
    }

    private String buildAccommodationNotificationText(AccommodationResponseDto responseDto,
                                                      String status) {
        return status + SEPARATION
                + TYPE + responseDto.getType() + SEPARATION
                + SIZE + responseDto.getSize() + SEPARATION
                + COUNTRY + responseDto.getLocation().getCountry() + SEPARATION
                + CITY + responseDto.getLocation().getCity() + SEPARATION
                + STREET + responseDto.getLocation().getCity() + SEPARATION
                + DAILY_RATE + responseDto.getDailyRate() + SEPARATION
                + AVAILABILITY + responseDto.getAvailability() + SEPARATION;
    }

    private String buildBookingNotificationText(BookingResponseDto responseDto) {
        return BOOKING_ID + responseDto.getId() + SEPARATION
                + TYPE + responseDto.getAccommodation().getType() + SEPARATION
                + SIZE + responseDto.getAccommodation().getSize() + SEPARATION
                + COUNTRY + responseDto.getAccommodation().getLocation().getCountry() + SEPARATION
                + CITY + responseDto.getAccommodation().getLocation().getCity() + SEPARATION
                + STREET + responseDto.getAccommodation().getLocation().getCity() + SEPARATION
                + DAILY_RATE + responseDto.getAccommodation().getDailyRate() + SEPARATION
                + CHECK_IN_DATA + responseDto.getCheckInDate() + SEPARATION
                + CHECK_OUT_DATA + responseDto.getCheckOutDate() + SEPARATION
                + STATUS + responseDto.getStatus() + SEPARATION;
    }

    private String buildBookingNotificationText(Booking booking) {
        return BOOKING_ID + booking.getId() + SEPARATION
                + TYPE + booking.getAccommodation().getType() + SEPARATION
                + SIZE + booking.getAccommodation().getSize() + SEPARATION
                + COUNTRY + booking.getAccommodation().getLocation().getCountry() + SEPARATION
                + CITY + booking.getAccommodation().getLocation().getCity() + SEPARATION
                + STREET + booking.getAccommodation().getLocation().getCity() + SEPARATION
                + DAILY_RATE + booking.getAccommodation().getDailyRate() + SEPARATION
                + CHECK_IN_DATA + booking.getCheckInDate() + SEPARATION
                + CHECK_OUT_DATA + booking.getCheckOutDate() + SEPARATION
                + STATUS + booking.getStatus() + SEPARATION;
    }

    private String buildUrl(UriComponentsBuilder builder, String path, Long id) {
        return builder
                .pathSegment(path, id.toString())
                .buildAndExpand(id)
                .toUriString();
    }

    private String buildPaymentNotification(PaymentSuccessDto payment) {
        Booking booking
                = bookingRepository.findById(payment.bookingId()).orElseThrow(
                        () -> new EntityNotFoundException("Can't found booking by id: "
                                + payment.bookingId()));
        return PAYMENT_STATUS + payment.paymentStatus() + SEPARATION
                + AMOUNT_TOTAL + payment.amount() + SEPARATION
                + buildBookingNotificationText(booking);
    }
}
