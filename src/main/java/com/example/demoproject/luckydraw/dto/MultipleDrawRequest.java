package com.example.demoproject.luckydraw.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

/**
 * Request DTO for multiple draws
 */
public class MultipleDrawRequest {
    
    @NotNull(message = "Activity ID is required")
    private Long activityId;
    
    @NotNull(message = "Draw count is required")
    @Min(value = 1, message = "Draw count must be at least 1")
    @Max(value = 10, message = "Draw count cannot exceed 10")
    private Integer drawCount;

    public MultipleDrawRequest() {}

    public MultipleDrawRequest(Long activityId, Integer drawCount) {
        this.activityId = activityId;
        this.drawCount = drawCount;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public Integer getDrawCount() {
        return drawCount;
    }

    public void setDrawCount(Integer drawCount) {
        this.drawCount = drawCount;
    }
} 