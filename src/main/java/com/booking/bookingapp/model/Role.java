package com.booking.bookingapp.model;

import org.springframework.security.core.GrantedAuthority;

public class Role implements GrantedAuthority {
    private Long id;
    private RoleName role;

    @Override
    public String getAuthority() {
        return "ROLE_" + role.name();
    }

    public enum RoleName {
        USER,
        ADMIN,
        MANAGER,
        CUSTOMER
    }
}
