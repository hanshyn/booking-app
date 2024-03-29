package com.booking.bookingapp.repository.accommodation;

import com.booking.bookingapp.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
}
