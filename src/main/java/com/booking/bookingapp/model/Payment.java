package com.booking.bookingapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private Set<Status> statuses;

    @OneToOne
    private Booking booking;
    //private String urlSession;
    //private String idSession;
    @OneToOne
    private Booking payment;
}
