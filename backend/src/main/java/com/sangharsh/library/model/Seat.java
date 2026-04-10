package com.sangharsh.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Seat entity - represents a physical seat in the library
 */
@Entity
@Table(name = "seats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g., A1, A2, B1, B2...
    @Column(nullable = false, unique = true, length = 10)
    private String seatNumber;

    // Section/Zone like "Ground Floor", "First Floor", "Quiet Zone"
    @Column(length = 50)
    private String section;

    // Description or features (e.g., "Near window", "With power outlet")
    @Column(length = 200)
    private String description;

    // Is seat active/available in the system
    @Column(nullable = false)
    private boolean active = true;

    // One seat can have many bookings over time
    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings;
}
