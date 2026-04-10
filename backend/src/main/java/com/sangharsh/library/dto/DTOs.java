package com.sangharsh.library.dto;

import com.sangharsh.library.model.Booking;
import com.sangharsh.library.model.User;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Data Transfer Objects for API requests and responses
 */
public class DTOs {

    // ==================== AUTH DTOs ====================

    @Data
    public static class RegisterRequest {
        private String fullName;
        private String email;
        private String password;
        private String phone;
    }

    @Data
    public static class LoginRequest {
        private String email;
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String email;
        private String fullName;
        private String role;
        private Long userId;

        public AuthResponse(String token, String email, String fullName, String role, Long userId) {
            this.token = token;
            this.email = email;
            this.fullName = fullName;
            this.role = role;
            this.userId = userId;
        }
    }

    // ==================== SEAT DTOs ====================

    @Data
    public static class SeatRequest {
        private String seatNumber;
        private String section;
        private String description;
    }

    @Data
    public static class SeatAvailabilityResponse {
        private Long id;
        private String seatNumber;
        private String section;
        private String description;
        private boolean available; // computed based on date/time
    }

    // ==================== BOOKING DTOs ====================

    @Data
    public static class BookingRequest {
        private Long seatId;
        private LocalDate bookingDate;
        private LocalTime startTime;
        private LocalTime endTime;
    }

    @Data
    public static class BookingResponse {
        private Long id;
        private String seatNumber;
        private String section;
        private String userName;
        private String userEmail;
        private LocalDate bookingDate;
        private LocalTime startTime;
        private LocalTime endTime;
        private double totalHours;
        private BigDecimal totalAmount;
        private BigDecimal hourlyRate;
        private String status;
        private String paymentStatus;
        private LocalDateTime createdAt;

        public static BookingResponse from(Booking b) {
            BookingResponse r = new BookingResponse();
            r.id = b.getId();
            r.seatNumber = b.getSeat().getSeatNumber();
            r.section = b.getSeat().getSection();
            r.userName = b.getUser().getFullName();
            r.userEmail = b.getUser().getEmail();
            r.bookingDate = b.getBookingDate();
            r.startTime = b.getStartTime();
            r.endTime = b.getEndTime();
            r.totalHours = b.getTotalHours();
            r.totalAmount = b.getTotalAmount();
            r.hourlyRate = b.getHourlyRate();
            r.status = b.getStatus().name();
            r.paymentStatus = b.getPaymentStatus().name();
            r.createdAt = b.getCreatedAt();
            return r;
        }
    }

    // ==================== PRICING DTOs ====================

    @Data
    public static class PricingRequest {
        private BigDecimal hourlyRate;
        private String description;
    }

    // ==================== USER DTOs ====================

    @Data
    public static class UserResponse {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private String role;
        private boolean active;
        private LocalDateTime createdAt;

        public static UserResponse from(User u) {
            UserResponse r = new UserResponse();
            r.id = u.getId();
            r.fullName = u.getFullName();
            r.email = u.getEmail();
            r.phone = u.getPhone();
            r.role = u.getRole().name();
            r.active = u.isActive();
            r.createdAt = u.getCreatedAt();
            return r;
        }
    }

    // ==================== DASHBOARD DTOs ====================

    @Data
    public static class DashboardStats {
        private long totalSeats;
        private long totalUsers;
        private long todayBookings;
        private long totalBookings;
        private BigDecimal currentHourlyRate;
    }

    // ==================== GENERIC RESPONSE ====================

    @Data
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }

        public static ApiResponse ok(String message, Object data) {
            return new ApiResponse(true, message, data);
        }

        public static ApiResponse error(String message) {
            return new ApiResponse(false, message, null);
        }
    }
}
