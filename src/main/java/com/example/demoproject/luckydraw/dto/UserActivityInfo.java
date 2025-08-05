package com.example.demoproject.luckydraw.dto;

/**
 * DTO for user activity information
 */
public class UserActivityInfo {
    private final String username;
    private final Long activityId;
    private final Integer maxDraws;
    private final Integer currentDraws;
    private final Integer remainingDraws;

    public UserActivityInfo(String username, Long activityId, Integer maxDraws, Integer currentDraws) {
        this.username = username;
        this.activityId = activityId;
        this.maxDraws = maxDraws;
        this.currentDraws = currentDraws;
        this.remainingDraws = maxDraws - currentDraws;
    }

    public String getUsername() {
        return username;
    }

    public Long getActivityId() {
        return activityId;
    }

    public Integer getMaxDraws() {
        return maxDraws;
    }

    public Integer getCurrentDraws() {
        return currentDraws;
    }

    public Integer getRemainingDraws() {
        return remainingDraws;
    }
} 