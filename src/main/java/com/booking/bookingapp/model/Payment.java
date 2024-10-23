package com.booking.bookingapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE payments SET is_deleted = true WHERE payment_id = ?")
@Where(clause = "is_deleted = false")
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Column(name = "payment_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private PaymentStatuses status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @Column(name = "session_url", nullable = false)
    private String sessionUrl;

    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    public enum PaymentStatuses {
        PENDING,
        PAID,
        FAILED,
        EXPIRED,
        CANCELED
    }
}
