package com.sangharsh.library.controller;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Public pricing endpoint - anyone can view current rates
 */
@RestController
@RequestMapping("/api/pricing")
@CrossOrigin(origins = "*")
public class PricingController {

    @Autowired
    private AdminService adminService;

    /**
     * GET /api/pricing/current
     * Get current hourly rate (public endpoint)
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentPricing() {
        try {
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Current pricing",
                    adminService.getCurrentPricing()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }
}
