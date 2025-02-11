package com.booking.bookingapp.repository.payment;

import com.booking.bookingapp.model.Payment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/add-payments-and-bookings-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PaymentRepositoryTest {
    private static final Long VALID_USER_ID = 1L;
    private static final int EXPECTED_TWO = 2;
    private static final Long INVALID_USER_ID = 100L;
    private static final int PAGE_NUMBER = 0;
    private static final int PAGE_SIZE = 10;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    @DisplayName("Find all payments by user id: result one payment")
    void findAllPaymentsByUserId_ValidUserId_ReturnTwoPayments() {
        Page<Payment> actual = paymentRepository
                .findAllBy_UserId(VALID_USER_ID, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        Assertions.assertEquals(EXPECTED_TWO, actual.getTotalElements());
    }

    @Test
    @DisplayName("Find all payment by user id: result null payments")
    void findAllPaymentsByUserId_InvalidUserId_ReturnNullPayments() {
        Page<Payment> actual = paymentRepository
                .findAllBy_UserId(INVALID_USER_ID, PageRequest.of(PAGE_NUMBER, PAGE_SIZE));

        Assertions.assertTrue(actual.isEmpty());
    }

    /*
    * @Query(
            value = "SELECT payment from Payment payment JOIN payment.booking booking"
                    + " JOIN booking.user user where user.id = :user_id"
    )
    Page<Payment> findAllBy_UserId(
            @Param("user_id") Long id, Pageable pageable);*/
}
