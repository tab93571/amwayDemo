package com.example.demoproject.luckydraw.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for single draw
 */
public class DrawRequest {
    
    @NotNull(message = "Activity ID is required")
    private Long activityId;

    public DrawRequest() {}

    public DrawRequest(Long activityId) {
        this.activityId = activityId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
} 