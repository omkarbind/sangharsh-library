package com.sangharsh.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Booking entity - stores seat reservation details
 */
@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The user who made this booking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // The seat being booked
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    // Date of booking
    @Column(nullable = false)
    private LocalDate bookingDate;

    // Start and end time
    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    // Total hours and calculated amount
    @Column(nullable = false)
    private double totalHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    // Hourly rate at time of booking (snapshot)
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    // Status: PENDING, CONFIRMED, CANCELLED, COMPLETED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.CONFIRMED;

    // Payment status (simulated)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum BookingStatus {
        PENDING, CONFIRMED, CANCELLED, COMPLETED
    }

    public enum PaymentStatus {
        PENDING, PAID, REFUNDED
    }
}
