package com.example.demoproject.auth.repository;

import com.example.demoproject.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthUserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find user by username
     * @param username The username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Check if user exists by username
     * @param username The username to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);
}