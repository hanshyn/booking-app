package com.booking.bookingapp.repository.payment;

import com.booking.bookingapp.model.Booking;
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

    @Query(value = "SELECT payment FROM Payment payment JOIN payment.booking booking "
            + "where booking.user.id = :user_id and booking.status in (:statuses)")
    Payment findBy_UserId_AndStatusBooking(
            @Param("user_id") Long id,
            @Param("statuses") List<Booking.Status> statuses);

    @Query(value = "SELECT count(payment.id) from Payment payment "
            + "JOIN payment.booking booking where  booking.user.id = :user_id "
            + "and payment.status in (:statuses)"
            + "group by payment.id"
    )
    Long countAllByUserIdAndPaymentStatuses(
            @Param("user_id") Long id,
            @Param("statuses") List<Payment.PaymentStatuses> statuses
    );

    /*
    @Query(value = "SELECT count(booking.accommodation.id) from Booking booking "
            + "where booking.accommodation.id = :accommodationId "
            + "and booking.status in (:statuses)"
            + "group by booking.accommodation.id"
    )
    Optional<Long> countAllByAccommodationIdAndStatuses(
            @Param("accommodationId") Long id,
            @Param("statuses") List<Booking.Status> statuses
    );
    * */

    @Query(
            value = "SELECT payment from Payment payment JOIN payment.booking booking"
                    + " JOIN booking.user user where user.id = :user_id"
    )
    Page<Payment> findAllBy_UserId(
            @Param("user_id") Long id, Pageable pageable);

    boolean existsByBookingId_Id(Long bookingId);

    /*
    ELECT bsh FROM Bookshelf bsh "
          + "LEFT JOIN bsh.books b "
          + "WHERE b.author = :authorName"


    SELECT payments FROM payments join public.bookings b on b.booking_id = payments.booking_id
         where payment_status = 'PENDING' and status = 'PENDING' and user_id = 1

         @Query(value = "SELECT count(booking.accommodation.id) from Booking booking "
            + "where booking.accommodation.id = :accommodationId "
            + "and booking.status in (:statuses)"
            + "group by booking.accommodation.id"
    )
    Long countAllByAccommodationIdAndStatuses (
            @Param("accommodationId") Long id,
            @Param("statuses") List<Booking.Status> statuses
    );
    */
}
