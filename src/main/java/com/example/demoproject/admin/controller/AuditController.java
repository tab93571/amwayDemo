package com.example.demoproject.admin.controller;

import com.example.demoproject.admin.dto.AuditLogResponse;
import com.example.demoproject.admin.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/audit")
@Tag(name = "Admin Audit", description = "Admin operations for viewing audit logs")
public class AuditController {

    @Autowired
    private AuditService auditService;

    @GetMapping
    @Operation(summary = "Get all audit logs", description = "Retrieve all audit logs")
    public ResponseEntity<List<AuditLogResponse>> getAllAuditLogs() {
        List<AuditLogResponse> logs = auditService.getAllAuditLogs().stream()
            .map(AuditLogResponse::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/entity/{entityType}")
    @Operation(summary = "Get audit logs by entity type", description = "Retrieve audit logs for a specific entity type")
    public ResponseEntity<List<AuditLogResponse>> getAuditLogsByEntityType(@PathVariable String entityType) {
        List<AuditLogResponse> logs = auditService.getAuditLogsByEntityType(entityType).stream()
            .map(AuditLogResponse::new)
            .collect(Collectors.toList());
        return ResponseEntity.ok(logs);
    }
} 