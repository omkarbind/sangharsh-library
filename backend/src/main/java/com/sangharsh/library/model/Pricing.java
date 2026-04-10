package com.sangharsh.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Pricing entity - admin sets the hourly rate here
 */
@Entity
@Table(name = "pricing")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pricing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Price per hour in INR
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    // Optional description for this pricing rule
    @Column(length = 200)
    private String description;

    // Whether this pricing is currently active
    @Column(nullable = false)
    private boolean active = true;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
}
