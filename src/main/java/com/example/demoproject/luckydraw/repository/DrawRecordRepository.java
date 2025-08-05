package com.example.demoproject.luckydraw.repository;

import com.example.demoproject.auth.entity.User;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.DrawRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository for DrawRecord entity
 */
@Repository
public interface DrawRecordRepository extends JpaRepository<DrawRecord, Long> {
    
    /**
     * Count draws by user and activity
     * @param user User entity
     * @param activity Activity entity
     * @return Number of draws
     */
    long countByUserAndActivity(User user, Activity activity);
    
    /**
     * Find all draws by user and activity
     * @param user User entity
     * @param activity Activity entity
     * @return List of draw records
     */
    List<DrawRecord> findByUserAndActivityOrderByDrawTimeDesc(User user, Activity activity);
    
    /**
     * Find all draws by activity
     * @param activity Activity entity
     * @return List of draw records
     */
    List<DrawRecord> findByActivityOrderByDrawTimeDesc(Activity activity);
} 