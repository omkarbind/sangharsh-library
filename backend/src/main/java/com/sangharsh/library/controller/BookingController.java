package com.sangharsh.library.controller;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.model.User;
import com.sangharsh.library.repository.UserRepository;
import com.sangharsh.library.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Booking Controller - create, view, and cancel bookings
 * Base URL: /api/bookings
 */
@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    // Helper to get current user ID from JWT
    private Long getCurrentUserId(Authentication auth) {
        User user = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getId();
    }

    /**
     * POST /api/bookings
     * Create a new booking (authenticated users)
     */
    @PostMapping
    public ResponseEntity<?> createBooking(@RequestBody DTOs.BookingRequest request,
                                           Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            DTOs.BookingResponse booking = bookingService.createBooking(userId, request);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Booking confirmed!", booking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/bookings/my
     * Get current user's booking history
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyBookings(Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Bookings fetched",
                    bookingService.getUserBookings(userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/bookings/all
     * Get all bookings (admin only)
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok("All bookings",
                bookingService.getAllBookings()));
    }

    /**
     * PUT /api/bookings/{id}/cancel
     * Cancel a booking
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            boolean isAdmin = auth.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Booking cancelled",
                    bookingService.cancelBooking(id, userId, isAdmin)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * PUT /api/bookings/{id}/pay
     * Simulate payment for a booking
     */
    @PutMapping("/{id}/pay")
    public ResponseEntity<?> confirmPayment(@PathVariable Long id, Authentication auth) {
        try {
            Long userId = getCurrentUserId(auth);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Payment confirmed!",
                    bookingService.confirmPayment(id, userId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }
}
