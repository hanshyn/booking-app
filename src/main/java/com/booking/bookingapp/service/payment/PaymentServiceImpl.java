package com.booking.bookingapp.service.payment;

import com.booking.bookingapp.dto.payment.PaymentCancelDto;
import com.booking.bookingapp.dto.payment.PaymentRequestDto;
import com.booking.bookingapp.dto.payment.PaymentResponseDto;
import com.booking.bookingapp.dto.payment.PaymentSuccessDto;
import com.booking.bookingapp.exception.EntityNotFoundException;
import com.booking.bookingapp.exception.PaymentException;
import com.booking.bookingapp.mapper.PaymentMapper;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.Payment;
import com.booking.bookingapp.model.User;
import com.booking.bookingapp.repository.booking.BookingRepository;
import com.booking.bookingapp.repository.payment.PaymentRepository;
import com.booking.bookingapp.repository.user.UserRepository;
import com.booking.bookingapp.service.notification.NotificationService;
import com.booking.bookingapp.stripe.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Double CENT_TO_DOLLAR_CONVERSION_FACTOR = 1.0 / 100;
    private static final String SESSION_EXPIRED = "expired";
    private static final String SESSION_COMPLETE = "complete";
    private static final String MESSAGE_CANCEL
            = "Payment was canceled, but you can retry within 24 hours.";

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BookingRepository bookingRepository;
    private final StripeService stripeService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public PaymentResponseDto payment(PaymentRequestDto requestDto,
                          UriComponentsBuilder uriBuilder) {

        Booking booking = getBookingById(requestDto.bookingId());

        checkPaymentByBooking(booking);
        isBookingNotExpired(requestDto.bookingId());

        Session session = stripeService.createPaymentSession(uriBuilder, booking);

        Payment payment = paymentRepository.save(setingPayment(booking, session));

        return paymentMapper.toDto(payment);
    }

    @Override
    public PaymentSuccessDto handlerSuccess(String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);

            if (session.getStatus().equals(SESSION_COMPLETE)) {
                Payment payment = getPaymentBySessionID(session.getId());
                payment.setStatus(Payment.PaymentStatus.PAID);
                paymentRepository.save(payment);

                Booking booking = getBookingById(payment.getBooking().getId());
                booking.setStatus(Booking.Status.PAID);
                bookingRepository.save(booking);

                PaymentSuccessDto paymentSuccessDto = paymentMapper.toSuccessDto(payment);

                notificationService.notifyAboutPayment(paymentSuccessDto);

                return paymentSuccessDto;
            } else {
                throw new PaymentException("Success not complete");
            }
        } catch (StripeException e) {
            throw new PaymentException("Handler success invalid" + e.getMessage());
        }
    }

    @Override
    public PaymentCancelDto handlerCansel(String sessionId) {
        Payment payment = getPaymentBySessionID(sessionId);
        payment.setStatus(Payment.PaymentStatus.CANCELED);
        paymentRepository.save(payment);

        return new PaymentCancelDto(payment.getSessionUrl(), MESSAGE_CANCEL);
    }

    @Override
    public void checkedExpiredSession(Event event) {
        Session session = (Session) event.getDataObjectDeserializer().getObject()
                .orElseThrow(() -> new IllegalStateException("Object deserialization error"));
        log.debug("Event type: {} Session: {}, ", event.getType(), session.getId());

        if (SESSION_EXPIRED.equals(session.getStatus())) {
            Payment payment = getPaymentBySessionID(session.getId());
            payment.setStatus(Payment.PaymentStatus.EXPIRED);
            paymentRepository.save(payment);

            log.debug("Session Expired: {}", SESSION_EXPIRED);
        }
    }

    @Override
    public List<PaymentResponseDto> getHistoryPayment(Pageable pageable) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return paymentRepository.findAllBy_UserId(user.getId(), pageable).stream()
                .map(paymentMapper::toDto).toList();
    }

    @Override
    public PaymentResponseDto getById(Long id) {
        return paymentMapper.toDto(
                paymentRepository.findById(id).orElseThrow(
                        () -> new EntityNotFoundException("Can't found payment by id: " + id))
        );
    }

    @Override
    public List<PaymentResponseDto> getByUserId(Long id, Pageable pageable) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found user by id " + id));

        return paymentRepository.findAllBy_UserId(user.getId(), pageable).stream()
                .map(paymentMapper::toDto).toList();
    }

    private Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found booking by id: " + id)
        );
    }

    private Payment getPaymentBySessionID(String id) {
        return paymentRepository.findBySessionId(id).orElseThrow(
                () -> new EntityNotFoundException("Can't found payment by session id : " + id)
        );
    }

    private Payment setingPayment(Booking booking, Session session) {
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setSessionUrl(session.getUrl());
        payment.setSessionId(session.getId());
        payment.setAmount(BigDecimal.valueOf(Double.valueOf(session.getAmountTotal())
                * CENT_TO_DOLLAR_CONVERSION_FACTOR));
        payment.setStatus(Payment.PaymentStatus.PENDING);

        return payment;
    }

    private void checkPaymentByBooking(Booking booking) {
        List<Payment.PaymentStatus> statuses = List.of(Payment.PaymentStatus.PENDING,
                Payment.PaymentStatus.PAID,
                Payment.PaymentStatus.CANCELED,
                Payment.PaymentStatus.FAILED);

        if (!paymentRepository.findByBookingIdAndStatusIn(booking.getId(), statuses).isEmpty()) {
            throw new PaymentException("Payment already exists not completed");
        }
    }

    private void isBookingNotExpired(Long id) {
        if (paymentRepository.existsByBookingId_Id(id)) {
            throw new PaymentException("Booking already exist. Please choose a different booking.");
        }
    }
}

