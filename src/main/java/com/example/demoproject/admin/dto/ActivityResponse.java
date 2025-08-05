package com.example.demoproject.admin.dto;

import java.util.List;

public class ActivityResponse {
    
    private String activityId;
    private String name;
    private String description;
    private Integer maxDraws;
    private List<PrizeResponse> prizes;
    
    public ActivityResponse() {}
    
    public ActivityResponse(String activityId, String name, String description, Integer maxDraws,
                          List<PrizeResponse> prizes) {
        this.activityId = activityId;
        this.name = name;
        this.description = description;
        this.maxDraws = maxDraws;
        this.prizes = prizes;
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
    
    public List<PrizeResponse> getPrizes() {
        return prizes;
    }
    
    public void setPrizes(List<PrizeResponse> prizes) {
        this.prizes = prizes;
    }
} 