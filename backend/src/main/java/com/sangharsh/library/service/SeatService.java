package com.sangharsh.library.service;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.model.Seat;
import com.sangharsh.library.repository.BookingRepository;
import com.sangharsh.library.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Seat service - manages seat operations
 */
@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Get all active seats with availability for a given time slot
     */
    public List<DTOs.SeatAvailabilityResponse> getAvailableSeats(LocalDate date, LocalTime start, LocalTime end) {
        return seatRepository.findByActiveTrue().stream().map(seat -> {
            DTOs.SeatAvailabilityResponse res = new DTOs.SeatAvailabilityResponse();
            res.setId(seat.getId());
            res.setSeatNumber(seat.getSeatNumber());
            res.setSection(seat.getSection());
            res.setDescription(seat.getDescription());

            // Check if seat has any overlapping booking
            boolean booked = !bookingRepository
                    .findOverlappingBookings(seat.getId(), date, start, end)
                    .isEmpty();
            res.setAvailable(!booked);
            return res;
        }).collect(Collectors.toList());
    }

    /**
     * Get all seats (for admin)
     */
    public List<Seat> getAllSeats() {
        return seatRepository.findAll();
    }

    /**
     * Add a new seat (admin only)
     */
    public Seat addSeat(DTOs.SeatRequest request) {
        if (seatRepository.existsBySeatNumber(request.getSeatNumber())) {
            throw new RuntimeException("Seat number already exists: " + request.getSeatNumber());
        }
        Seat seat = new Seat();
        seat.setSeatNumber(request.getSeatNumber());
        seat.setSection(request.getSection());
        seat.setDescription(request.getDescription());
        seat.setActive(true);
        return seatRepository.save(seat);
    }

    /**
     * Toggle seat active status (soft delete)
     */
    public Seat toggleSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seat.setActive(!seat.isActive());
        return seatRepository.save(seat);
    }

    /**
     * Delete seat permanently (admin only)
     */
    public void deleteSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat not found"));
        seatRepository.delete(seat);
    }
}
