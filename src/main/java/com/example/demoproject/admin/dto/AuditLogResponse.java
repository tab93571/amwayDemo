package com.example.demoproject.admin.dto;

import com.example.demoproject.admin.entity.AuditLog;
import java.time.LocalDateTime;

/**
 * DTO for audit log responses
 */
public class AuditLogResponse {
    
    private final Long id;
    private final String adminUsername;
    private final String action;
    private final String entityType;
    private final Long entityId;
    private final String oldValues;
    private final String newValues;
    private final LocalDateTime timestamp;

    public AuditLogResponse(AuditLog auditLog) {
        this.id = auditLog.getId();
        this.adminUsername = auditLog.getAdminUser().getUsername();
        this.action = auditLog.getAction();
        this.entityType = auditLog.getEntityType();
        this.entityId = auditLog.getEntityId();
        this.oldValues = auditLog.getOldValues();
        this.newValues = auditLog.getNewValues();
        this.timestamp = auditLog.getTimestamp();
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public String getAction() {
        return action;
    }

    public String getEntityType() {
        return entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public String getOldValues() {
        return oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
} 