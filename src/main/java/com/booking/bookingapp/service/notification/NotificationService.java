package com.booking.bookingapp.service.notification;

import com.booking.bookingapp.dto.accommodation.AccommodationResponseDto;
import com.booking.bookingapp.dto.booking.BookingResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.model.Booking;

public interface NotificationService {

    void createdBooking(BookingResponseDto responseDto);

    void canceledBooking(Long telegramId,
                         BookingResponseDto responseDto,
                         Booking.Status status);

    void createdAccommodation(AccommodationResponseDto responseDto);

    void releasedAccommodation(AccommodationResponseDto responseDto);

    void notifyAboutPayment(PaymentSuccessDto paymentSuccessDto);

    void notifyNoExpiredBookings();
}
