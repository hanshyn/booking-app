package com.booking.bookingapp.repository.booking;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByUserId(Long id);

    List<Booking> findBookingsByUserId(Long id);

    List<Booking> findBookingsByUser(User user);
}
