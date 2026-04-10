package com.sangharsh.library.controller;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Seat Controller - manage seats and check availability
 * Base URL: /api/seats
 */
@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatController {

    @Autowired
    private SeatService seatService;

    /**
     * GET /api/seats/available?date=&startTime=&endTime=
     * Get available seats for a given date and time (public)
     */
    @GetMapping("/available")
    public ResponseEntity<?> getAvailableSeats(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Seats fetched",
                    seatService.getAvailableSeats(date, startTime, endTime)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/seats/all
     * Get all seats (admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllSeats() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok("All seats", seatService.getAllSeats()));
    }

    /**
     * POST /api/seats
     * Add a new seat (admin only)
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addSeat(@RequestBody DTOs.SeatRequest request) {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Seat added!", seatService.addSeat(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/seats/{id}/toggle
     * Enable/disable a seat (admin only)
     */
    @PutMapping("/{id}/toggle")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> toggleSeat(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Seat status updated!", seatService.toggleSeat(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * DELETE /api/seats/{id}
     * Delete a seat permanently (admin only)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteSeat(@PathVariable Long id) {
        try {
            seatService.deleteSeat(id);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Seat deleted!", null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }
}
