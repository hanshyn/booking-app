package com.booking.bookingapp.model;

import java.time.LocalDate;
import java.util.Set;

public class Booking {
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private Accommodation accommodation;
    private User user;
    private Set<Status> statuses;
}
