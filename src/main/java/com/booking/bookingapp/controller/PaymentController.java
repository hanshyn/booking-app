package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.payment.PaymentCancelDto;
import com.booking.bookingapp.dto.payment.PaymentRequestDto;
import com.booking.bookingapp.dto.payment.PaymentResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.service.payment.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping()
    public PaymentResponseDto payment(@RequestBody @Valid PaymentRequestDto requestDto,
                                      UriComponentsBuilder uriBuilder) {
        return paymentService.payment(requestDto, uriBuilder);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping() List<PaymentResponseDto> getByUserId(@RequestParam("user_id") Long id,
                                                       Pageable pageable) {
        return paymentService.getByUserId(id, pageable);
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my")
    public List<PaymentResponseDto> getHistoryPayment(Pageable pageable) {
        return paymentService.getHistoryPayment(pageable);
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/my/{id}")
    public PaymentResponseDto getById(@PathVariable Long id) {
        return paymentService.getById(id);
    }

    @PostMapping("/webhook")
    public void webhook(@RequestBody String payload,
                          @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("webhook");
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            paymentService.checkedExpiredSession(event);
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/success")
    public PaymentSuccessDto success(@RequestParam("session_id") String sessionId) {
        return paymentService.handlerSuccess(sessionId);
    }

    @GetMapping("/cancel")
    public PaymentCancelDto cancel(@RequestParam("session_id") String sessionId) {
        return paymentService.handlerCansel(sessionId);
    }
}
