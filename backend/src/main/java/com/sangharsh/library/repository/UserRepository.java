package com.sangharsh.library.repository;

import com.sangharsh.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * JPA Repository for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used for login)
    Optional<User> findByEmail(String email);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Count active users
    long countByActiveTrue();
}
