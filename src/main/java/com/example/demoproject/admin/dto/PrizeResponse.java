package com.example.demoproject.admin.dto;

import java.time.LocalDateTime;
import java.math.BigDecimal;

public class PrizeResponse {
    
    private Long id;
    private String name;
    private String description;
    private Integer quantity;
    private Integer remainingQuantity;
    private BigDecimal probability;
    private Long activityId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public PrizeResponse() {}
    
    public PrizeResponse(Long id, String name, String description, Integer quantity, 
                       Integer remainingQuantity, BigDecimal probability, Long activityId,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.remainingQuantity = remainingQuantity;
        this.probability = probability;
        this.activityId = activityId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public Integer getRemainingQuantity() {
        return remainingQuantity;
    }
    
    public void setRemainingQuantity(Integer remainingQuantity) {
        this.remainingQuantity = remainingQuantity;
    }
    
    public BigDecimal getProbability() {
        return probability;
    }
    
    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }
    
    public Long getActivityId() {
        return activityId;
    }
    
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
} 