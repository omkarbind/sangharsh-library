package com.sangharsh.library.controller;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Admin Controller - dashboard, pricing, user management
 * Base URL: /api/admin
 * All endpoints require ADMIN role
 */
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * GET /api/admin/dashboard
     * Get library dashboard statistics
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok("Dashboard stats",
                adminService.getDashboardStats()));
    }

    /**
     * GET /api/admin/users
     * Get all registered users
     */
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(DTOs.ApiResponse.ok("Users fetched",
                adminService.getAllUsers()));
    }

    /**
     * PUT /api/admin/users/{id}/toggle
     * Enable or disable a user account
     */
    @PutMapping("/users/{id}/toggle")
    public ResponseEntity<?> toggleUser(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("User status updated",
                    adminService.toggleUserStatus(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/admin/pricing
     * Set a new hourly pricing
     */
    @PostMapping("/pricing")
    public ResponseEntity<?> setPricing(@RequestBody DTOs.PricingRequest request) {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Pricing updated!",
                    adminService.setPricing(request)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * GET /api/pricing/current (also mapped at admin level)
     * Get current active pricing
     */
    @GetMapping("/pricing")
    public ResponseEntity<?> getCurrentPricing() {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Current pricing",
                    adminService.getCurrentPricing()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }
}
