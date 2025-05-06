package com.booking.bookingapp.controller;

import com.booking.bookingapp.dto.payment.PaymentCancelDto;
import com.booking.bookingapp.dto.payment.PaymentRequestDto;
import com.booking.bookingapp.dto.payment.PaymentResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.exception.PaymentException;
import com.booking.bookingapp.service.payment.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payments",
        description = "Endpoints for managing payments and handling Stripe webhooks")
@RequiredArgsConstructor
@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @Operation(summary = "Process a payment",
            description = "Initiates a payment session and returns payment details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment initiated successfully",
                    content = @Content(
                            schema = @Schema(implementation = PaymentResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment request data")
    })
    @PostMapping()
    public PaymentResponseDto payment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payment request details", required = true,
                    content = @Content(schema = @Schema(implementation = PaymentRequestDto.class)))
            @RequestBody @Valid PaymentRequestDto requestDto, UriComponentsBuilder uriBuilder) {
        return paymentService.payment(requestDto, uriBuilder);
    }

    @Operation(summary = "Get payments by user ID",
            description = "Retrieves a paginated list of payments for a specific user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of payments retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access forbidden for non-admin users")
    })
    @PreAuthorize("hasAnyRole({'ROLE_ADMIN', 'ROLE_MANAGER'})")
    @GetMapping() List<PaymentResponseDto> getByUserId(
            @Parameter(description = "User ID to fetch payments for", required = true)
            @RequestParam("user_id") Long id, Pageable pageable) {
        return paymentService.getByUserId(id, pageable);
    }

    @Operation(summary = "Get current user's payment history",
            description = "Retrieves payment history for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment history retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access")
    })

    @GetMapping("/my")
    public List<PaymentResponseDto> getHistoryPayment(Pageable pageable) {
        return paymentService.getHistoryPayment(pageable);
    }

    @Operation(summary = "Get specific payment by ID",
            description = "Retrieves details of a specific payment for the authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment details retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Payment not found")
    })

    @GetMapping("/my/{id}")
    public PaymentResponseDto getById(
            @Parameter(description = "ID of the payment to retrieve", required = true)
            @PathVariable Long id) {
        return paymentService.getById(id);
    }

    @Operation(summary = "Stripe webhook endpoint",
            description = "Endpoint for handling Stripe events (webhook)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event handled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid signature in request header")
    })
    @PostMapping("/webhook")
    public void webhook(
            @Parameter(description = "Payload of the Stripe event", required = true)
            @RequestBody String payload,
            @Parameter(description = "Stripe signature header", required = true)
            @RequestHeader("Stripe-Signature") String sigHeader) {
        System.out.println("webhook");
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            paymentService.checkedExpiredSession(event);
        } catch (SignatureVerificationException e) {
            throw new PaymentException(e.getMessage());
        }
    }

    @Operation(summary = "Payment success handler",
            description = "Handles successful payment sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment success details returned",
                    content = @Content(schema = @Schema(implementation = PaymentSuccessDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid session ID")
    })
    @GetMapping("/success")
    public PaymentSuccessDto success(
            @Parameter(description = "Session ID of the successful payment", required = true)
            @RequestParam("session_id") String sessionId) {
        return paymentService.handlerSuccess(sessionId);
    }

    @Operation(summary = "Payment cancel handler",
            description = "Handles canceled payment sessions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Payment cancellation details returned",
                    content = @Content(schema = @Schema(implementation = PaymentCancelDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid session ID")
    })
    @GetMapping("/cancel")
    public PaymentCancelDto cancel(
            @Parameter(description = "Session ID of the canceled payment", required = true)
            @RequestParam("session_id") String sessionId) {
        return paymentService.handlerCansel(sessionId);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
