package com.booking.bookingapp.repository;

import com.booking.bookingapp.model.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenitiesRepository extends JpaRepository<Amenities, Long> {
}
