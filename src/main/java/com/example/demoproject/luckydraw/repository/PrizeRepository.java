package com.example.demoproject.luckydraw.repository;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Prize entity
 */
@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {
    
    /**
     * Find all prizes for a specific activity
     * @param activityId Activity identifier
     * @return List of prizes
     */
    List<Prize> findByActivity(Activity activity);
    
    /**
     * Find prize by ID with pessimistic lock for inventory management
     * @param id Prize ID
     * @return Prize with lock
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Prize p WHERE p.id = :id")
    Prize findByIdWithLock(@Param("id") Long id);

} 