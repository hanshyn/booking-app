package com.booking.bookingapp.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StripePaymentService {
    @Value("${stripe.apikey}")
    private String stripeApiKey;

    @PostConstruct
    void initStripe() {
        Stripe.apiKey = stripeApiKey;
    }

}
