package com.booking.bookingapp.model;

public class Status {
    private Long id;
    private StatusName status;

    public enum StatusName {
        PENDING,
        CONFIRMED,
        CANCELED,
        EXPIRED,
        PAID
    }
}
