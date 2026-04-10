# 📚 Sangharsh K Library – Seat Booking System
**Location:** Greater Noida, Uttar Pradesh

A full-stack library seat booking web application with:
- JWT-secured REST API (Spring Boot + MySQL)
- Clean, responsive frontend (HTML/CSS/JS)
- Role-based access: **User** and **Admin**

---

## 🗂️ Project Structure

```
sangharsh-library/
├── backend/                          # Spring Boot application
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/sangharsh/library/
│       │   ├── LibraryApplication.java
│       │   ├── config/
│       │   │   ├── SecurityConfig.java         # Spring Security + CORS
│       │   │   ├── CustomUserDetailsService.java
│       │   │   └── DataInitializer.java        # Seeds default data
│       │   ├── controller/
│       │   │   ├── AuthController.java         # /api/auth/*
│       │   │   ├── SeatController.java         # /api/seats/*
│       │   │   ├── BookingController.java      # /api/bookings/*
│       │   │   ├── AdminController.java        # /api/admin/*
│       │   │   └── PricingController.java      # /api/pricing/*
│       │   ├── model/
│       │   │   ├── User.java
│       │   │   ├── Seat.java
│       │   │   ├── Booking.java
│       │   │   └── Pricing.java
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── SeatRepository.java
│       │   │   ├── BookingRepository.java
│       │   │   └── PricingRepository.java
│       │   ├── service/
│       │   │   ├── AuthService.java
│       │   │   ├── SeatService.java
│       │   │   ├── BookingService.java
│       │   │   └── AdminService.java
│       │   ├── dto/
│       │   │   └── DTOs.java                   # All request/response DTOs
│       │   └── security/
│       │       ├── JwtUtil.java
│       │       └── JwtAuthFilter.java
│       └── resources/
│           └── application.properties
│
├── frontend/                         # HTML/CSS/JS frontend
│   ├── index.html                    # Login / Register
│   ├── css/
│   │   └── style.css                 # All styles
│   ├── js/
│   │   ├── api.js                    # API client + utilities
│   │   └── layout.js                 # Sidebar/topbar renderer
│   └── pages/
│       ├── dashboard.html            # User dashboard
│       ├── book-seat.html            # Book a seat
│       ├── my-bookings.html          # Booking history + payment
│       ├── admin-dashboard.html      # Admin stats overview
│       ├── admin-seats.html          # Add/manage seats
│       ├── admin-bookings.html       # View/cancel all bookings
│       ├── admin-users.html          # Enable/disable members
│       └── admin-pricing.html        # Set hourly rate
│
└── database/
    └── schema.sql                    # Full MySQL schema + seed data
```

---

## ⚙️ Prerequisites

| Tool | Version |
|------|---------|
| Java | 17+ |
| Maven | 3.8+ |
| MySQL | 8.0+ |
| Any browser | Chrome / Firefox / Edge |

---

## 🚀 Setup Instructions

### Step 1 – MySQL Setup

```bash
# Log in to MySQL
mysql -u root -p

# Run the schema file
source /path/to/sangharsh-library/database/schema.sql;
```

Or import via MySQL Workbench / phpMyAdmin.

---

### Step 2 – Configure Backend

Open `backend/src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/sangharsh_library?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

---

### Step 3 – Run the Backend

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The backend starts at: **http://localhost:8080**

On first run, it automatically seeds:
- ✅ Admin account: `admin@sangharsh.com` / `admin123`
- ✅ 15 default seats (A1–A5, B1–B5, C1–C5)
- ✅ Default pricing: ₹20/hour

---

### Step 4 – Open the Frontend

Simply open `frontend/index.html` in your browser.

> **Tip:** Use VS Code's **Live Server** extension for best results,  
> or serve with Python: `python -m http.server 5500` from the `frontend/` folder.

---

## 🔑 Default Credentials

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@sangharsh.com | admin123 |
| User | Register a new account | – |

---

## 🌐 API Endpoints

### Authentication (Public)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT token |

### Seats
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/seats/available?date=&startTime=&endTime=` | Public | Get available seats |
| GET | `/api/seats/all` | Admin | Get all seats |
| POST | `/api/seats` | Admin | Add new seat |
| PUT | `/api/seats/{id}/toggle` | Admin | Enable/disable seat |
| DELETE | `/api/seats/{id}` | Admin | Delete seat |

### Bookings
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/bookings` | User | Create booking |
| GET | `/api/bookings/my` | User | My booking history |
| GET | `/api/bookings/all` | Admin | All bookings |
| PUT | `/api/bookings/{id}/cancel` | User/Admin | Cancel booking |
| PUT | `/api/bookings/{id}/pay` | User | Confirm payment |

### Pricing
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/pricing/current` | Public | Get current rate |
| POST | `/api/admin/pricing` | Admin | Update hourly rate |

### Admin
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/admin/dashboard` | Admin | Stats overview |
| GET | `/api/admin/users` | Admin | All users |
| PUT | `/api/admin/users/{id}/toggle` | Admin | Enable/disable user |

---

## 🔒 Authentication

All protected endpoints require a JWT token in the header:

```
Authorization: Bearer <your_jwt_token>
```

Token is returned on login/register and stored in `localStorage` by the frontend.

---

## 💡 Sample API Requests

### Register
json
POST /api/auth/register
{
  "fullName": "Rahul Kumar",
  "email": "rahul@example.com",
  "password": "password123",
  "phone": "9876543210"
}


### Login
json
POST /api/auth/login
{
  "email": "rahul@example.com",
  "password": "password123"
}


### Book a Seat
json
POST /api/bookings
Authorization: Bearer <token>
{
  "seatId": 1,
  "bookingDate": "2025-04-15",
  "startTime": "10:00",
  "endTime": "13:00"
}


### Set Pricing (Admin)
json
POST /api/admin/pricing
Authorization: Bearer <token>
{
  "hourlyRate": 25.00,
  "description": "Revised April 2025"
}


---

## 🎨 Features Summary

### User Side
- ✅ Register & Login with JWT
- ✅ View available seats in real-time by date + time
- ✅ Visual seat map (green = available, red = booked)
- ✅ Price preview before confirming
- ✅ Booking history with status
- ✅ Cancel bookings
- ✅ Simulated payment (click to confirm)

### Admin Panel
- ✅ Dashboard with live stats
- ✅ Add / enable / disable / delete seats
- ✅ View and cancel all bookings
- ✅ Manage user accounts
- ✅ Set hourly pricing with live calculator preview

### Technical
- ✅ Double-booking prevention via overlap query
- ✅ BCrypt password hashing
- ✅ JWT stateless authentication
- ✅ CORS configured for frontend access
- ✅ Role-based access control (RBAC)
- ✅ Mobile-responsive UI
- ✅ Auto-seeds default data on first run

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2 |
| Security | Spring Security + JWT (jjwt 0.11.5) |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 |
| Frontend | HTML5, CSS3, Vanilla JavaScript |
| Fonts | Playfair Display + DM Sans (Google Fonts) |
| Build | Maven |

---

## ⚠️ Notes

- Payment is **simulated** (no real payment gateway).
- JWT token expires in **24 hours** by default.
- Change `app.jwt.secret` in `application.properties` before deploying to production.
- Use HTTPS in production and restrict CORS to your domain.

---

*Built for Sangharsh K Library, Greater Noida, Uttar Pradesh* 🇮🇳
