package com.sangharsh.library.service;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.model.Pricing;
import com.sangharsh.library.repository.BookingRepository;
import com.sangharsh.library.repository.PricingRepository;
import com.sangharsh.library.repository.SeatRepository;
import com.sangharsh.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin service - dashboard stats, user management, pricing
 */
@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PricingRepository pricingRepository;

    /**
     * Get dashboard statistics
     */
    public DTOs.DashboardStats getDashboardStats() {
        DTOs.DashboardStats stats = new DTOs.DashboardStats();
        stats.setTotalSeats(seatRepository.countByActiveTrue());
        stats.setTotalUsers(userRepository.countByActiveTrue());
        stats.setTodayBookings(bookingRepository.countByBookingDate(LocalDate.now()));
        stats.setTotalBookings(bookingRepository.count());

        pricingRepository.findFirstByActiveTrueOrderByCreatedAtDesc()
                .ifPresent(p -> stats.setCurrentHourlyRate(p.getHourlyRate()));
        return stats;
    }

    /**
     * Get all users
     */
    public List<DTOs.UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(DTOs.UserResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * Toggle user active status
     */
    public DTOs.UserResponse toggleUserStatus(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setActive(!user.isActive());
        return DTOs.UserResponse.from(userRepository.save(user));
    }

    /**
     * Set new hourly pricing (deactivates old pricing)
     */
    public Pricing setPricing(DTOs.PricingRequest request) {
        // Deactivate all existing pricing
        pricingRepository.findAll().forEach(p -> {
            p.setActive(false);
            pricingRepository.save(p);
        });

        // Create new active pricing
        Pricing pricing = new Pricing();
        pricing.setHourlyRate(request.getHourlyRate());
        pricing.setDescription(request.getDescription());
        pricing.setActive(true);
        pricing.setUpdatedAt(LocalDateTime.now());
        return pricingRepository.save(pricing);
    }

    /**
     * Get current active pricing
     */
    public Pricing getCurrentPricing() {
        return pricingRepository.findFirstByActiveTrueOrderByCreatedAtDesc()
                .orElseThrow(() -> new RuntimeException("No pricing configured"));
    }
}
