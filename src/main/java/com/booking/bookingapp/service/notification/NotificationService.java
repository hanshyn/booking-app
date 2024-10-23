package com.booking.bookingapp.service.notification;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.model.Booking;
import org.springframework.web.util.UriComponentsBuilder;

public interface NotificationService {

    void createdBooking(BookingResponseDto responseDto,
                        UriComponentsBuilder urlBuilder);

    void canceledBooking(Long telegramId,
                         BookingResponseDto responseDto,
                         UriComponentsBuilder urlBuilder,
                         Booking.Status status);

    void createdAccommodation(AccommodationResponseDto responseDto,
                              UriComponentsBuilder urlBuilder);

    void releasedAccommodation(AccommodationResponseDto responseDto,
                               UriComponentsBuilder urlBuilder);

    void notifyAboutPayment(PaymentSuccessDto paymentSuccessDto);

    void notifyNoExpiredBookings();
}
