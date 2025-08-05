package com.example.demoproject.luckydraw.entity;

import jakarta.persistence.*;
import java.util.List;

/**
 * Activity entity for managing lucky draw activities
 */
@Entity
@Table(name = "activities")
public class Activity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;
    
    @Column(nullable = false)
    private Integer maxDraws;
    
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Prize> prizes;
    
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DrawRecord> drawRecords;

    public Activity() {}

    public Activity(String name, String description, Integer maxDraws) {
        this.name = name;
        this.description = description;
        this.maxDraws = maxDraws;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    
    public List<Prize> getPrizes() {
        return prizes;
    }
    
    public void setPrizes(List<Prize> prizes) {
        this.prizes = prizes;
    }
    
    public List<DrawRecord> getDrawRecords() {
        return drawRecords;
    }
    
    public void setDrawRecords(List<DrawRecord> drawRecords) {
        this.drawRecords = drawRecords;
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