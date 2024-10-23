package com.booking.bookingapp.stripe.service;

import static java.time.temporal.ChronoUnit.DAYS;

import com.booking.bookingapp.exception.PaymentException;
import com.booking.bookingapp.model.Booking;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class StripeServiceImpl implements StripeService {
    private static final String CHECKOUT_SESSION_ID = "{CHECKOUT_SESSION_ID}";
    private static final String PATH_SUCCESS = "/booking/payments/success";
    private static final String PATH_CANCEL = "/booking/payments/cancel";
    private static final String QUERY_PARAM = "session_id";
    private static final String CURRENCY = "USD";
    private static final int CONVERSION_TO_CENTS = 100;

    @Value("${site.url}")
    private String url;

    @SneakyThrows
    @Override
    public Session createPaymentSession(UriComponentsBuilder uriBuilder, Booking booking) {
        try {
            SessionCreateParams params = SessionCreateParams.builder().addLineItem(
                    SessionCreateParams.LineItem.builder().setPriceData(
                            SessionCreateParams.LineItem.PriceData.builder().setCurrency(CURRENCY)
                                    .setProductData(
                                            SessionCreateParams.LineItem.PriceData.ProductData
                                                    .builder()
                                                    .setName(booking.getId().toString())
                                                    .build()
                                    )
                                    .setUnitAmount(amount(booking))
                                    .build()
                            )
                            .setQuantity(daysBetween(booking))
                            .build()
                    )
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(buildUrl(url, PATH_SUCCESS))
                    .setCancelUrl(buildUrl(url, PATH_CANCEL))
                    .build();

            Session session = Session.create(params);

            System.out.println("URL: " + session.getUrl());
            System.out.println("SessionId: " + session.getId());
            System.out.println("URL CANCEL: " + session.getCancelUrl());
            System.out.println("URL SUCCESS: " + session.getSuccessUrl());

            return session;
        } catch (StripeException e) {
            throw new PaymentException("Can't creating Stripe session: " + e.getMessage());
        }
    }

    private Long daysBetween(Booking booking) {
        return DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
    }

    private Long amount(Booking booking) {
        return booking.getAccommodation().getDailyRate().multiply(
                new BigDecimal(CONVERSION_TO_CENTS)
        ).longValue();
    }

    private String buildUrl(String uri, String patch) {
        UriComponentsBuilder uriComponentsBuilderSuccess =
                UriComponentsBuilder.fromHttpUrl(uri);
        return uriComponentsBuilderSuccess
                .path(patch)
                .queryParam(QUERY_PARAM, CHECKOUT_SESSION_ID)
                .build()
                .toUriString();
    }
}
