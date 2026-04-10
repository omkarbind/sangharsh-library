package com.sangharsh.library.repository;

import com.sangharsh.library.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * JPA Repository for Booking entity
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Get all bookings by user
    List<Booking> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Get bookings for a seat on a specific date
    List<Booking> findBySeatIdAndBookingDate(Long seatId, LocalDate date);

    // Check for overlapping bookings (to prevent double booking)
    @Query("SELECT b FROM Booking b WHERE b.seat.id = :seatId " +
           "AND b.bookingDate = :date " +
           "AND b.status != 'CANCELLED' " +
           "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findOverlappingBookings(
        @Param("seatId") Long seatId,
        @Param("date") LocalDate date,
        @Param("startTime") LocalTime startTime,
        @Param("endTime") LocalTime endTime
    );

    // Get all bookings for admin view
    List<Booking> findAllByOrderByCreatedAtDesc();

    // Count today's bookings
    long countByBookingDate(LocalDate date);

    // Get bookings by status
    List<Booking> findByStatusOrderByCreatedAtDesc(Booking.BookingStatus status);
}
