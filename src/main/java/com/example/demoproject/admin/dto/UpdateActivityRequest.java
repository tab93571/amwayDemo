package com.example.demoproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

public class UpdateActivityRequest {
    
    @NotBlank(message = "Activity name is required")
    private String name;
    
    @NotBlank(message = "Activity description is required")
    private String description;
    
    @Min(value = 1, message = "Max draws must be at least 1")
    private Integer maxDraws;
    
    public UpdateActivityRequest() {}
    
    public UpdateActivityRequest(String name, String description, Integer maxDraws) {
        this.name = name;
        this.description = description;
        this.maxDraws = maxDraws;
    }
    
    // Getters and Setters
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