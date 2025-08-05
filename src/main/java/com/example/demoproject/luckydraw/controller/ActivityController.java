package com.example.demoproject.luckydraw.controller;

import com.example.demoproject.luckydraw.dto.*;
import com.example.demoproject.luckydraw.service.ActivityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST Controller for activity information operations
 */
@RestController
@RequestMapping("/api/luckydraw/activity")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityInfoService activityInfoService;

    /**
     * Get all available activities for users
     * @return List of available activities
     */
    @GetMapping("/list")
    public ResponseEntity<List<ActivityInfo>> getAvailableActivities() {
        try {
            List<ActivityInfo> activities = activityInfoService.getAvailableActivities();
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }


} 