package com.booking.bookingapp.model;

import java.util.Set;

public class User {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private Set<Role> role;
}
