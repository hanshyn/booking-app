package com.booking.bookingapp.model;

public class Type {
    private Long id;
    private TypeName type;

    public enum TypeName {
        HOUSE,
        APARTMENT,
        CONDO,
        VACATION_HOME
    }
}
