package com.booking.bookingapp.repository.payment;

import com.booking.bookingapp.model.Payment;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingIdAndStatusIn(Long id, Collection<Payment.PaymentStatuses> status);

    Optional<Payment> findBySessionId(String sessionId);

    @Query(
            value = "SELECT payment from Payment payment JOIN payment.booking booking"
                    + " JOIN booking.user user where user.id = :user_id"
    )
    Page<Payment> findAllBy_UserId(
            @Param("user_id") Long id, Pageable pageable);

    boolean existsByBookingId_Id(Long bookingId);
}
