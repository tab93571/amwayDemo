package com.example.demoproject.admin.service;

import com.example.demoproject.admin.dto.*;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demoproject.luckydraw.service.UserActivityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserActivityService userActivityService;

    public List<ActivityResponse> getAllActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
            .map(activity -> new ActivityResponse(
                activity.getId().toString(), // Use id as activityId
                activity.getName(),
                activity.getDescription(),
                activity.getMaxDraws(),
                null // prizes - will be loaded separately if needed
            ))
            .collect(Collectors.toList());
    }

    public ActivityResponse updateActivity(String activityId, UpdateActivityRequest request) {
        Long id = Long.parseLong(activityId);
        Activity activity = activityRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));

        // Create audit data for old values
        ActivityAuditData oldData = new ActivityAuditData(
            activity.getName(),
            activity.getDescription(),
            activity.getMaxDraws()
        );

        // Update activity
        activity.setName(request.getName());
        activity.setDescription(request.getDescription());
        activity.setMaxDraws(request.getMaxDraws());

        Activity savedActivity = activityRepository.save(activity);

        // Create audit data for new values
        ActivityAuditData newData = new ActivityAuditData(
            savedActivity.getName(),
            savedActivity.getDescription(),
            savedActivity.getMaxDraws()
        );

        // Log the audit
        try {
            String adminUsername = userActivityService.getCurrentUsername();
            auditService.logAction(adminUsername, "UPDATE_ACTIVITY", "ACTIVITY", id, oldData, newData);
        } catch (Exception e) {
            // Log error but don't fail the update
            System.err.println("Error logging activity update audit: " + e.getMessage());
        }

        return new ActivityResponse(
            savedActivity.getId().toString(), // Use id as activityId
            savedActivity.getName(),
            savedActivity.getDescription(),
            savedActivity.getMaxDraws(),
            null // prizes - will be loaded separately if needed
        );
    }

    /**
     * Inner class for audit data
     */
    private static class ActivityAuditData {
        private final String name;
        private final String description;
        private final Integer maxDraws;

        public ActivityAuditData(String name, String description, Integer maxDraws) {
            this.name = name;
            this.description = description;
            this.maxDraws = maxDraws;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Integer getMaxDraws() { return maxDraws; }
    }


} 