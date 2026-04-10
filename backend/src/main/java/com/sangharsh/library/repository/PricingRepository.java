package com.sangharsh.library.repository;

import com.sangharsh.library.model.Pricing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * JPA Repository for Pricing entity
 */
@Repository
public interface PricingRepository extends JpaRepository<Pricing, Long> {

    // Get the currently active pricing
    Optional<Pricing> findFirstByActiveTrueOrderByCreatedAtDesc();
}
