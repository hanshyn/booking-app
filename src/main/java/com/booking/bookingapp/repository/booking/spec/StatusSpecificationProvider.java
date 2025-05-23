package com.booking.bookingapp.repository.booking.spec;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class StatusSpecificationProvider implements SpecificationProvider<Booking> {
    public static final String STATUS = "status";

    @Override
    public String getKey() {
        return STATUS;
    }

    @Override
    public Specification<Booking> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(STATUS).in(Arrays.stream(params).toArray()));
    }
}
