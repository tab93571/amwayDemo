package com.example.demoproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateActivityRequest {
    
    @NotNull(message = "Activity ID is required")
    private String activityId;
    
    @NotBlank(message = "Activity name is required")
    private String name;
    
    @NotBlank(message = "Activity description is required")
    private String description;
    
    @Min(value = 1, message = "Max draws must be at least 1")
    private Integer maxDraws;
    
    public CreateActivityRequest() {}
    
    public CreateActivityRequest(String activityId, String name, String description, Integer maxDraws) {
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.maxDraws = maxDraws;
    }
    
    // Getters and Setters
    public String getActivityId() {
        return activityId;
    }
    
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getMaxDraws() {
        return maxDraws;
    }
    
    public void setMaxDraws(Integer maxDraws) {
        this.maxDraws = maxDraws;
    }
} 