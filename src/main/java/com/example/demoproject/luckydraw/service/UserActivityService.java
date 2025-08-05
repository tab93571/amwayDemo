package com.example.demoproject.luckydraw.service;

import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.luckydraw.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.security.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Service for user-related operations
 */
@Service
public class UserActivityService {

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private DrawRecordRepository drawRecordRepository;


    /**
     * Get current user's username
     */
    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Get current user's ID from authentication context (more efficient)
     */
    public Long getCurrentUserId() {
        Object details = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (details instanceof Map) {
            Long userId = (Long) ((Map<?, ?>) details).get("userId");
            if (userId != null) {
                return userId;
            }
        }
        throw new IllegalStateException("User ID not found in authentication context");
    }

    /**
     * Get user activity information for current user and activity
     * @param activityId Activity identifier
     * @return User activity information
     */
    public UserActivityInfo getUserActivityInfo(Long activityId) {
        Long userId = getCurrentUserId();
        User user = authUserRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        long userDrawCount = drawRecordRepository.countByUserAndActivity(user, activity);
        
        return new UserActivityInfo(
            user.getUsername(),
            activityId,
            activity.getMaxDraws(),
            (int) userDrawCount
        );
    }
} 