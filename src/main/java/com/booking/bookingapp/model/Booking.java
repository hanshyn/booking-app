package com.booking.bookingapp.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE bookings SET is_deleted = true WHERE booking_id = ?")
@Where(clause = "is_deleted = false")
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

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.REMOVE)
    private Payment payment;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public enum Status {
        PENDING,
        PAID,
        CONFIRMED,
        COMPLETED,
        CANCELED,
        EXPIRED,
    }
}
