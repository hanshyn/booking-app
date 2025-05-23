package com.booking.bookingapp.stripe.service;

import com.booking.bookingapp.model.Booking;
import com.stripe.model.checkout.Session;
import org.springframework.web.util.UriComponentsBuilder;

public interface StripeService {
    Session createPaymentSession(UriComponentsBuilder uriBuilder, Booking booking);
}
