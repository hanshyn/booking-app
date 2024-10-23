package com.booking.bookingapp.service.payment;

import com.booking.bookingapp.dto.payment.PaymentCancelDto;
import com.booking.bookingapp.dto.payment.PaymentRequestDto;
import com.booking.bookingapp.dto.payment.PaymentResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.stripe.model.Event;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.web.util.UriComponentsBuilder;

public interface PaymentService {
    PaymentResponseDto payment(PaymentRequestDto requestDto,
                               UriComponentsBuilder uriBuilder);

    PaymentSuccessDto handlerSuccess(String sessionId);

    PaymentCancelDto handlerCansel(String sessionId);

    void checkedExpiredSession(Event event);

    List<PaymentResponseDto> getHistoryPayment(Pageable pageable);

    PaymentResponseDto getById(Long id);

    List<PaymentResponseDto> getByUserId(Long id, Pageable pageable);
}
