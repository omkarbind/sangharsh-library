package com.sangharsh.library.service;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.model.Booking;
import com.sangharsh.library.model.Pricing;
import com.sangharsh.library.model.Seat;
import com.sangharsh.library.model.User;
import com.sangharsh.library.repository.BookingRepository;
import com.sangharsh.library.repository.PricingRepository;
import com.sangharsh.library.repository.SeatRepository;
import com.sangharsh.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Booking service - handles all booking operations including double-booking prevention
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PricingRepository pricingRepository;

    /**
     * Create a new booking for a user
     */
    public DTOs.BookingResponse createBooking(Long userId, DTOs.BookingRequest request) {

        // Validate time range
        if (!request.getStartTime().isBefore(request.getEndTime())) {
            throw new RuntimeException("Start time must be before end time!");
        }

        // Validate booking date is not in the past
        if (request.getBookingDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot book for a past date!");
        }

        // Check minimum 1 hour booking
        long minutes = Duration.between(request.getStartTime(), request.getEndTime()).toMinutes();
        if (minutes < 60) {
            throw new RuntimeException("Minimum booking duration is 1 hour!");
        }

        // Fetch seat
        Seat seat = seatRepository.findById(request.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found!"));

        if (!seat.isActive()) {
            throw new RuntimeException("This seat is not available for booking!");
        }

        // Check for overlapping bookings (prevent double booking)
        List<Booking> conflicts = bookingRepository.findOverlappingBookings(
                seat.getId(), request.getBookingDate(),
                request.getStartTime(), request.getEndTime()
        );
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Seat is already booked for this time slot!");
        }

        // Get current pricing
        Pricing pricing = pricingRepository.findFirstByActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No pricing configured. Please contact admin."));

        // Calculate total hours and amount
        double totalHours = minutes / 60.0;
        BigDecimal totalAmount = pricing.getHourlyRate().multiply(BigDecimal.valueOf(totalHours));

        // Fetch user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        // Create booking
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setSeat(seat);
        booking.setBookingDate(request.getBookingDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setTotalHours(totalHours);
        booking.setTotalAmount(totalAmount);
        booking.setHourlyRate(pricing.getHourlyRate());
        booking.setStatus(Booking.BookingStatus.CONFIRMED);
        booking.setPaymentStatus(Booking.PaymentStatus.PENDING);

        Booking saved = bookingRepository.save(booking);
        return DTOs.BookingResponse.from(saved);
    }

    /**
     * Get all bookings for a specific user
     */
    public List<DTOs.BookingResponse> getUserBookings(Long userId) {
        return bookingRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(DTOs.BookingResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Get all bookings (admin)
     */
    public List<DTOs.BookingResponse> getAllBookings() {
        return bookingRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(DTOs.BookingResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Cancel a booking
     */
    public DTOs.BookingResponse cancelBooking(Long bookingId, Long userId, boolean isAdmin) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        // Only the owner or admin can cancel
        if (!isAdmin && !booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to cancel this booking!");
        }

        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled!");
        }

        booking.setStatus(Booking.BookingStatus.CANCELLED);
        booking.setPaymentStatus(Booking.PaymentStatus.REFUNDED);
        return DTOs.BookingResponse.from(bookingRepository.save(booking));
    }

    /**
     * Simulate payment confirmation
     */
    public DTOs.BookingResponse confirmPayment(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found!"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized!");
        }

        booking.setPaymentStatus(Booking.PaymentStatus.PAID);
        return DTOs.BookingResponse.from(bookingRepository.save(booking));
    }
}
