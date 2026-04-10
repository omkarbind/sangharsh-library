package com.sangharsh.library.config;

import com.sangharsh.library.model.Pricing;
import com.sangharsh.library.model.Seat;
import com.sangharsh.library.model.User;
import com.sangharsh.library.repository.PricingRepository;
import com.sangharsh.library.repository.SeatRepository;
import com.sangharsh.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * DataInitializer - Seeds default admin, seats, and pricing on first run
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private PricingRepository pricingRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Create default admin if not exists
        if (!userRepository.existsByEmail("admin@sangharsh.com")) {
            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail("admin@sangharsh.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setPhone("9999999999");
            admin.setRole(User.Role.ADMIN);
            admin.setActive(true);
            userRepository.save(admin);
            System.out.println("✅ Default admin created: admin@sangharsh.com / admin123");
        }

        // Create default pricing if not exists
        if (pricingRepository.count() == 0) {
            Pricing pricing = new Pricing();
            pricing.setHourlyRate(new BigDecimal("20.00")); // ₹20/hour
            pricing.setDescription("Standard hourly rate");
            pricing.setActive(true);
            pricingRepository.save(pricing);
            System.out.println("✅ Default pricing set: ₹20/hour");
        }

        // Seed some seats if none exist
        if (seatRepository.count() == 0) {
            String[][] seats = {
                {"A1", "Ground Floor", "Near window, power outlet"},
                {"A2", "Ground Floor", "Near window"},
                {"A3", "Ground Floor", "Central area"},
                {"A4", "Ground Floor", "Central area"},
                {"A5", "Ground Floor", "Corner seat"},
                {"B1", "First Floor", "Quiet zone, power outlet"},
                {"B2", "First Floor", "Quiet zone"},
                {"B3", "First Floor", "Quiet zone"},
                {"B4", "First Floor", "Near AC"},
                {"B5", "First Floor", "Corner seat"},
                {"C1", "AC Section", "Premium AC seat"},
                {"C2", "AC Section", "Premium AC seat"},
                {"C3", "AC Section", "Premium AC seat"},
                {"C4", "AC Section", "Power outlet available"},
                {"C5", "AC Section", "Corner seat with AC"},
            };
            for (String[] s : seats) {
                Seat seat = new Seat();
                seat.setSeatNumber(s[0]);
                seat.setSection(s[1]);
                seat.setDescription(s[2]);
                seat.setActive(true);
                seatRepository.save(seat);
            }
            System.out.println("✅ 15 default seats created");
        }
    }
}
