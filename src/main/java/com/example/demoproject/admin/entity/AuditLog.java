package com.example.demoproject.admin.entity;

import com.example.demoproject.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Audit log entity for tracking admin actions
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "admin_user_id")
    private User adminUser;
    
    @Column(nullable = false)
    private String action; // e.g., "UPDATE_ACTIVITY", "UPDATE_PRIZE"
    
    @Column(nullable = false)
    private String entityType; // e.g., "ACTIVITY", "PRIZE"
    
    @Column(nullable = false)
    private Long entityId; // ID of the entity being modified
    
    @Column(columnDefinition = "TEXT")
    private String oldValues; // JSON string of old values
    
    @Column(columnDefinition = "TEXT")
    private String newValues; // JSON string of new values
    
    @Column(nullable = false)
    private LocalDateTime timestamp;

    public AuditLog() {}

    public AuditLog(User adminUser, String action, String entityType, Long entityId, String oldValues, String newValues) {
        this.adminUser = adminUser;
        this.action = action;
        this.entityType = entityType;
        this.entityId = entityId;
        this.oldValues = oldValues;
        this.newValues = newValues;
        this.timestamp = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getOldValues() {
        return oldValues;
    }

    public void setOldValues(String oldValues) {
        this.oldValues = oldValues;
    }

    public String getNewValues() {
        return newValues;
    }

    public void setNewValues(String newValues) {
        this.newValues = newValues;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
} 