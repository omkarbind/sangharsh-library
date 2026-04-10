package com.sangharsh.library.controller;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller - handles user registration and login
 * Base URL: /api/auth
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/auth/register
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DTOs.RegisterRequest request) {
        try {
            DTOs.AuthResponse response = authService.register(request);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Registration successful!", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error(e.getMessage()));
        }
    }

    /**
     * POST /api/auth/login
     * Login and receive JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody DTOs.LoginRequest request) {
        try {
            DTOs.AuthResponse response = authService.login(request);
            return ResponseEntity.ok(DTOs.ApiResponse.ok("Login successful!", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(DTOs.ApiResponse.error("Invalid email or password!"));
        }
    }
}
