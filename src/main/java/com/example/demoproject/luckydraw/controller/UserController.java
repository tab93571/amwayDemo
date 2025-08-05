package com.example.demoproject.luckydraw.controller;

import com.example.demoproject.luckydraw.dto.*;
import com.example.demoproject.luckydraw.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for user-specific operations
 */
@RestController
@RequestMapping("/api/luckydraw/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserActivityService userActivityService;

    /**
     * Get user activity information for current user
     * @param activityId Activity identifier
     * @return User activity information
     */
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<UserActivityInfo> getUserActivityInfo(@PathVariable long activityId) {
        try {
            UserActivityInfo info = userActivityService.getUserActivityInfo(activityId);
            return ResponseEntity.ok(info);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 