package com.booking.bookingapp.repository.accommodation;

import com.booking.bookingapp.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
