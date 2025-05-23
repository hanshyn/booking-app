package com.booking.bookingapp.repository.booking;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.bookingapp.model.Booking;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = "classpath:database/payment/delete-all-table.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:database/payment/add-payments-and-bookings-to-db.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookingRepositoryTest {
    private static final Long VALID_ID = 1L;
    private static final int EXPECTED_ONE = 1;
    private static final Long INVALID_ID = 100L;

    @Autowired
    private BookingRepository bookingRepository;

    @Test
    @DisplayName("Count bookings by accommodation ID and statuses: expected 1")
    void countAllByAccommodationIdAndStatuses_ValidAccommodationId_ReturnOneCount() {
        List<Booking.Status> statuses = List.of(Booking.Status.CONFIRMED, Booking.Status.PENDING);
        Optional<Long> actual
                = bookingRepository.countAllByAccommodationIdAndStatuses(VALID_ID, statuses);

        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(EXPECTED_ONE);
    }

    @Test
    @DisplayName("Count bookings: invalid accommodation ID should return empty")
    void countAllByAccommodationIdAndStatuses_InvalidAccommodationId_ReturnEmpty() {
        List<Booking.Status> statuses = List.of(Booking.Status.CONFIRMED, Booking.Status.PENDING);
        Optional<Long> actual
                = bookingRepository.countAllByAccommodationIdAndStatuses(INVALID_ID, statuses);

        assertThat(actual).isNotPresent();
    }
}
