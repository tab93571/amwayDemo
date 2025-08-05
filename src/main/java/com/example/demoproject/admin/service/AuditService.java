package com.example.demoproject.admin.service;

import com.example.demoproject.admin.entity.AuditLog;
import com.example.demoproject.admin.repository.AuditLogRepository;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for audit logging
 */
@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Log an admin action
     */
    public void logAction(String adminUsername, String action, String entityType, Long entityId, Object oldValues, Object newValues) throws JsonProcessingException {

        User adminUser = authUserRepository.findByUsername(adminUsername)
                .orElseThrow(() -> new IllegalArgumentException("Admin user not found: " + adminUsername));

        String oldValuesJson = oldValues != null ? objectMapper.writeValueAsString(oldValues) : null;
        String newValuesJson = newValues != null ? objectMapper.writeValueAsString(newValues) : null;

        AuditLog auditLog = new AuditLog(adminUser, action, entityType, entityId, oldValuesJson, newValuesJson);
        auditLogRepository.save(auditLog);

    }

    /**
     * Get all audit logs
     */
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    /**
     * Get audit logs by entity type
     */
    public List<AuditLog> getAuditLogsByEntityType(String entityType) {
        return auditLogRepository.findByEntityTypeOrderByTimestampDesc(entityType);
    }
} 