package com.example.demoproject.luckydraw.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * Prize entity for lucky draw system
 */
@Entity
@Table(name = "prizes")
public class Prize {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal probability;
    
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;


    public Prize(String name, String description, Integer quantity, BigDecimal probability, Activity activity) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.probability = probability;
        this.activity = activity;
    }

    public Prize() {

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

    public BigDecimal getProbability() {
        return probability;
    }

    public void setProbability(BigDecimal probability) {
        this.probability = probability;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
} 