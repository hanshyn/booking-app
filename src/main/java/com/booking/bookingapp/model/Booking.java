package com.booking.bookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Long id;

    @Column(name = "in_date", nullable = false)
    private LocalDate checkInDate;

    @Column(name = "out_date", nullable = false)
    private LocalDate checkOutDate;

    @JoinColumn(name = "accommodation_id", nullable = false)
    @ManyToOne
    private Accommodation accommodation;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        PENDING,
        CONFIRMED,
        CANCELED,
        EXPIRED
    }
}
