package com.sangharsh.library.service;

import com.sangharsh.library.dto.DTOs;
import com.sangharsh.library.model.User;
import com.sangharsh.library.repository.UserRepository;
import com.sangharsh.library.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Authentication service - handles registration and login
 */
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Register a new user
     */
    public DTOs.AuthResponse register(DTOs.RegisterRequest request) {
        // Check if email already registered
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        // Create and save user
        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(User.Role.USER);
        user.setActive(true);

        User saved = userRepository.save(user);

        // Generate token and return response
        UserDetails userDetails = userDetailsService.loadUserByUsername(saved.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new DTOs.AuthResponse(token, saved.getEmail(), saved.getFullName(),
                saved.getRole().name(), saved.getId());
    }

    /**
     * Login and get JWT token
     */
    public DTOs.AuthResponse login(DTOs.LoginRequest request) {
        // Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // Load user and generate token
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return new DTOs.AuthResponse(token, user.getEmail(), user.getFullName(),
                user.getRole().name(), user.getId());
    }
}
