package com.booking.bookingapp.service.payment;

import static java.time.temporal.ChronoUnit.DAYS;

import com.booking.bookingapp.dto.payment.PaymentRequestDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StripePaymentService {
    private static final String DOMAIN = "http://localhost:8080/booking";
    private static final String CURRENCY = "USD";
    private static final int CONVERSION_TO_CENTS = 100;

    private final BookingRepository bookingRepository;

    @Value("${stripe.apikey}")
    private String stripeApiKey;

    @Value("${site.url}")
    private String url;

    @SneakyThrows
    public String createSessionParams(PaymentRequestDto requestDto) {
        Stripe.apiKey = stripeApiKey;

        Booking booking = bookingRepository.findById(requestDto.bookingId()).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't found booking by id: " + requestDto.bookingId()
                )
        );

        Long daysBetween = DAYS.between(booking.getCheckInDate(), booking.getCheckOutDate());
        System.out.println("daysBetween: " + daysBetween);

        Long amount = booking.getAccommodation().getDailyRate().multiply(
                new BigDecimal(CONVERSION_TO_CENTS)
        ).longValue();
        System.out.println("amount: " + amount + " BigDecimal: "
                + booking.getAccommodation().getDailyRate());

        SessionCreateParams params = SessionCreateParams.builder().addLineItem(
                SessionCreateParams.LineItem.builder().setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder().setCurrency(CURRENCY)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(booking.getId().toString())
                                                .build()
                                )
                                .setUnitAmount(amount)
                                .build()
                        )
                        .setQuantity(daysBetween)
                        .build()
                )
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(url + "/booking/payments/success")
                .setCancelUrl(url + "/booking/payments/cancel")
                .build();

        Session session = Session.create(params);

        System.out.println("URL: " + session.getUrl());
        System.out.println("SessionId: " + session.getId());

        return session.toJson();
    }

    /*@SneakyThrows
    public String createSession() {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(DOMAIN + "/payments/success")
                .setCancelUrl(DOMAIN + "/payments/cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPrice("price_1P3eN4CdOQGrgBcs0ZSGuwdJ") //bd
                                .setQuantity(6L)
                                .build()
                )
                .build();
        Session session = Session.create(params);
        System.out.println(session.getUrl());
        System.out.println(session.getId());
        return session.toJson();
    }*/

    /*@SneakyThrows
    public String createCardToken() {
        Stripe.apiKey = stripeApiKey;
        TokenCreateParams params =
                TokenCreateParams.builder()
                        .setCard(
                                TokenCreateParams.Card.builder()
                                        .setNumber("4242424242424242")
                                        .setExpMonth("5")
                                        .setExpYear("25")
                                        .setCvc("123")
                                        .build()
                        )
                        .build();
        Token token = Token.create(params);
        return token.getId();
    }*/

    /*@SneakyThrows
    public String createParam() {
        Stripe.apiKey = stripeApiKey;
        ChargeCreateParams params =
                ChargeCreateParams.builder()
                        .setAmount(1099L)
                        .setCurrency("usd")
                        .setSource("tok_visa")
                        .build();
        Charge charge = Charge.create(params);
        return charge.toJson();
    }*/

    /*

    @SneakyThrows
    public String createPaymentItems() {
        Stripe.apiKey = stripeApiKey;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(2000L)
                        .setCurrency("usd")
                        .putMetadata(
                                "bookingId", "124"
                        )
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        //return paymentIntent.getClientSecret();
        return paymentIntent.toJson();
    }
*/
}
