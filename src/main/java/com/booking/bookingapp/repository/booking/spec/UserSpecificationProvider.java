package com.booking.bookingapp.repository.booking.spec;

import com.booking.bookingapp.model.Booking;
import com.booking.bookingapp.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class UserSpecificationProvider implements SpecificationProvider<Booking> {
    private static final String USER_ID = "user_id";

    @Override
    public String getKey() {
        return USER_ID;
    }

    @Override
    public Specification<Booking> getSpecification(String[] params) {
        return ((root, query, criteriaBuilder) ->
                root.get(USER_ID).in(Arrays.stream(params).toArray()));
    }
}
