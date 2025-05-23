package com.booking.bookingapp.repository.booking.spec;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecificationProvider implements SpecificationProvider<Booking> {
    private static final String USER = "user";
    private static final String ID = "id";

    @Override
    public String getKey() {
        return ID;
    }

    @Override
    public Specification<Booking> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(USER).get(ID).in(Arrays.stream(params).toArray()));
    }
}
