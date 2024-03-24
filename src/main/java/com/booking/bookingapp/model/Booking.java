package com.booking.bookingapp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
//@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate checkIn;
    private LocalDate checkOut;
    @OneToOne
    private Accommodation accommodation;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @ManyToMany
    private Set<Status> statuses;
}
