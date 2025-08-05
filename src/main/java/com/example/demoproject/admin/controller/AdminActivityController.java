package com.example.demoproject.admin.controller;

import com.example.demoproject.admin.dto.*;
import com.example.demoproject.admin.service.AdminActivityService;
import com.example.demoproject.luckydraw.dto.UserDrawHistory;
import com.example.demoproject.admin.service.AdminDrawHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/activities")
@Tag(name = "Admin Activity Management", description = "Admin operations for activity management")
public class AdminActivityController {

    @Autowired
    private AdminActivityService adminActivityService;

    @Autowired
    private AdminDrawHistoryService adminDrawHistoryService;



    @GetMapping
    @Operation(summary = "Get all activities", description = "Retrieve all activities")
    public ResponseEntity<List<ActivityResponse>> getAllActivities() {
        return ResponseEntity.ok(adminActivityService.getAllActivities());
    }

    @PutMapping("/{activityId}")
    @Operation(summary = "Update activity", description = "Update activity details")
    public ResponseEntity<ActivityResponse> updateActivity(
            @PathVariable String activityId,
            @Valid @RequestBody UpdateActivityRequest request) {
        return ResponseEntity.ok(adminActivityService.updateActivity(activityId, request));
    }

    @GetMapping("/{activityId}/history")
    @Operation(summary = "Get draw history for activity", description = "Retrieve draw history for a specific activity")
    public ResponseEntity<UserDrawHistory> getDrawHistory(@PathVariable Long activityId) {
        UserDrawHistory history = adminDrawHistoryService.getDrawHistoryForActivity(activityId);
        return ResponseEntity.ok(history);
    }


} 