-- ============================================================
-- Sangharsh K Library - Database Schema
-- Location: Greater Noida, Uttar Pradesh
-- Database: MySQL 8.0+
-- ============================================================

-- Create and select database
CREATE DATABASE IF NOT EXISTS sangharsh_library
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE sangharsh_library;

-- ============================================================
-- TABLE: users
-- Stores library members and admin accounts
-- ============================================================
CREATE TABLE IF NOT EXISTS users (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name   VARCHAR(100)  NOT NULL,
    email       VARCHAR(100)  NOT NULL UNIQUE,
    password    VARCHAR(255)  NOT NULL,          -- BCrypt hashed
    phone       VARCHAR(15),
    role        ENUM('USER','ADMIN') NOT NULL DEFAULT 'USER',
    active      BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_users_email (email),
    INDEX idx_users_role  (role)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: seats
-- Physical seats in the library
-- ============================================================
CREATE TABLE IF NOT EXISTS seats (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    seat_number  VARCHAR(10)  NOT NULL UNIQUE,   -- e.g. A1, B3
    section      VARCHAR(50),                     -- e.g. Ground Floor, AC Zone
    description  VARCHAR(200),                    -- e.g. Near window
    active       BOOLEAN      NOT NULL DEFAULT TRUE,
    INDEX idx_seats_number (seat_number),
    INDEX idx_seats_active (active)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: pricing
-- Hourly rate configuration (only one active at a time)
-- ============================================================
CREATE TABLE IF NOT EXISTS pricing (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    hourly_rate  DECIMAL(10,2) NOT NULL,
    description  VARCHAR(200),
    active       BOOLEAN       NOT NULL DEFAULT TRUE,
    created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_pricing_active (active)
) ENGINE=InnoDB;

-- ============================================================
-- TABLE: bookings
-- Seat reservation records
-- ============================================================
CREATE TABLE IF NOT EXISTS bookings (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT        NOT NULL,
    seat_id         BIGINT        NOT NULL,
    booking_date    DATE          NOT NULL,
    start_time      TIME          NOT NULL,
    end_time        TIME          NOT NULL,
    total_hours     DOUBLE        NOT NULL,
    hourly_rate     DECIMAL(10,2) NOT NULL,       -- snapshot at booking time
    total_amount    DECIMAL(10,2) NOT NULL,
    status          ENUM('PENDING','CONFIRMED','CANCELLED','COMPLETED') NOT NULL DEFAULT 'CONFIRMED',
    payment_status  ENUM('PENDING','PAID','REFUNDED') NOT NULL DEFAULT 'PENDING',
    created_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (seat_id) REFERENCES seats(id) ON DELETE CASCADE,

    -- Prevent exact duplicate bookings
    UNIQUE KEY uq_booking (seat_id, booking_date, start_time, end_time),

    INDEX idx_bookings_user        (user_id),
    INDEX idx_bookings_seat        (seat_id),
    INDEX idx_bookings_date        (booking_date),
    INDEX idx_bookings_status      (status)
) ENGINE=InnoDB;

-- ============================================================
-- SEED DATA
-- ============================================================

-- Default Admin (password: admin123 — BCrypt encoded)
INSERT IGNORE INTO users (full_name, email, password, phone, role, active)
VALUES ('Admin', 'admin@sangharsh.com',
        '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
        '9999999999', 'ADMIN', TRUE);

-- Default Pricing: ₹20/hour
INSERT IGNORE INTO pricing (hourly_rate, description, active)
VALUES (20.00, 'Standard hourly rate', TRUE);

-- Default Seats (15 seats across 3 sections)
INSERT IGNORE INTO seats (seat_number, section, description, active) VALUES
('A1', 'Ground Floor', 'Near window, power outlet', TRUE),
('A2', 'Ground Floor', 'Near window', TRUE),
('A3', 'Ground Floor', 'Central area', TRUE),
('A4', 'Ground Floor', 'Central area', TRUE),
('A5', 'Ground Floor', 'Corner seat', TRUE),
('B1', 'First Floor',  'Quiet zone, power outlet', TRUE),
('B2', 'First Floor',  'Quiet zone', TRUE),
('B3', 'First Floor',  'Quiet zone', TRUE),
('B4', 'First Floor',  'Near AC', TRUE),
('B5', 'First Floor',  'Corner seat', TRUE),
('C1', 'AC Section',   'Premium AC seat', TRUE),
('C2', 'AC Section',   'Premium AC seat', TRUE),
('C3', 'AC Section',   'Premium AC seat', TRUE),
('C4', 'AC Section',   'Power outlet available', TRUE),
('C5', 'AC Section',   'Corner seat with AC', TRUE);

-- ============================================================
-- USEFUL QUERIES FOR REFERENCE
-- ============================================================

-- View all bookings with user and seat info:
-- SELECT b.id, u.full_name, u.email, s.seat_number, s.section,
--        b.booking_date, b.start_time, b.end_time,
--        b.total_hours, b.total_amount, b.status, b.payment_status
-- FROM bookings b
-- JOIN users u ON b.user_id = u.id
-- JOIN seats  s ON b.seat_id = s.id
-- ORDER BY b.created_at DESC;

-- Check overlapping bookings for a seat:
-- SELECT * FROM bookings
-- WHERE seat_id = 1
--   AND booking_date = '2025-04-10'
--   AND status != 'CANCELLED'
--   AND start_time < '12:00' AND end_time > '10:00';

-- Today's revenue:
-- SELECT SUM(total_amount) as revenue
-- FROM bookings
-- WHERE booking_date = CURDATE() AND status = 'CONFIRMED';
