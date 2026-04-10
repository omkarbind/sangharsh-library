package com.sangharsh.library.repository;

import com.sangharsh.library.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * JPA Repository for Seat entity
 */
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    // Get all active seats
    List<Seat> findByActiveTrue();

    // Find seat by number
    java.util.Optional<Seat> findBySeatNumber(String seatNumber);

    // Check if seat number exists
    boolean existsBySeatNumber(String seatNumber);

    // Count active seats
    long countByActiveTrue();
}
