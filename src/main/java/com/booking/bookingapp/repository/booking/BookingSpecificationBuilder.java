package com.booking.bookingapp.repository.booking;

import com.booking.bookingapp.dto.booking.BookingSearchParameters;
import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.repository.SpecificationBuilder;
import com.booking.bookingapp.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookingSpecificationBuilder implements SpecificationBuilder<Booking> {
    private static final String USER_ID = "id";
    private static final String STATUS = "status";

    private final SpecificationProviderManager<Booking> bookingSpecificationProviderManager;

    @Override
    public Specification<Booking> build(BookingSearchParameters searchParameters) {
        Specification<Booking> spec = Specification.where(null);

        if (searchParameters.userId() != null && searchParameters.userId().length > 0) {
            spec = spec.and(bookingSpecificationProviderManager.getSpecificationProvider(USER_ID)
                    .getSpecification(searchParameters.userId()));
        }

        if (searchParameters.status() != null && searchParameters.status().length > 0) {
            spec = spec.and(bookingSpecificationProviderManager.getSpecificationProvider(STATUS)
                    .getSpecification(searchParameters.status()));
        }

        return spec;
    }
}
