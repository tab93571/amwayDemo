package com.example.demoproject.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class UpdatePrizeRequest {
    
    @NotBlank(message = "Prize name is required")
    private String name;
    
    @NotBlank(message = "Prize description is required")
    private String description;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be non-negative")
    private Integer quantity;
    
    @NotNull(message = "Probability is required")
    @DecimalMin(value = "0.0", message = "Probability must be non-negative")
    @DecimalMax(value = "1.0", message = "Probability cannot exceed 1.0")
    private BigDecimal probability;
    
    public UpdatePrizeRequest() {}
    
    public UpdatePrizeRequest(String name, String description, Integer quantity, BigDecimal probability) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.probability = probability;
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
    
    public Integer getQuantity() {
        return quantity;
    }
    
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    
    public BigDecimal getProbability() {
        return probability;
    }
    
    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }
} 