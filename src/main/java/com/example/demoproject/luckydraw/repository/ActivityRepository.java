package com.example.demoproject.luckydraw.repository;

import com.example.demoproject.luckydraw.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Repository for Activity entity
 */
@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    
    /**
     * Find activity by activity ID
     * @param id Activity identifier
     * @return Optional activity
     */
    Optional<Activity> findById(Long id);
    
    /**
     * Check if activity exists
     * @param id Activity identifier
     * @return true if activity exists
     */
    boolean existsById(Long id);
    
    boolean existsByName(String name);
} 