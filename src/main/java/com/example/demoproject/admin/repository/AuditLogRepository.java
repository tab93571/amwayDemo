package com.example.demoproject.admin.repository;

import com.example.demoproject.admin.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    
    /**
     * Find audit logs by entity type
     */
    List<AuditLog> findByEntityTypeOrderByTimestampDesc(String entityType);
} 