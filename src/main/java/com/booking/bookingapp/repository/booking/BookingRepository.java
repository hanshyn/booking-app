package com.booking.bookingapp.repository.booking;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BookingRepository extends JpaRepository<Booking, Long>,
        JpaSpecificationExecutor<Booking> {
    List<Booking> findBookingByUserId(Long id);

    List<Booking> findBookingsByUserId(Long id);

    List<Booking> findBookingsByUser(User user);
}
