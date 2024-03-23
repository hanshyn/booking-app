package com.booking.bookingapp.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class Accommodation {
    private Long id;
    private Set<Type> type;
    private Address location;
    private String size;
    private List<String> amenities;
    private BigDecimal dailyRate;
    private int availability;
}
