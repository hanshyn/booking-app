package com.booking.bookingapp.repository;

import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(BookingSearchParameters searchParameters);
}
