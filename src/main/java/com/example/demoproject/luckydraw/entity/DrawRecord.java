package com.example.demoproject.luckydraw.entity;

import com.example.demoproject.auth.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Draw record entity for tracking lucky draw attempts
 */
@Entity
@Table(name = "draw_records")
public class DrawRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
    
    @ManyToOne
    @JoinColumn(name = "prize_id")
    private Prize prize;
    
    @Column(nullable = false)
    private LocalDateTime drawTime;

    public DrawRecord() {}

    public DrawRecord(User user, Activity activity, Prize prize) {
        this.user = user;
        this.activity = activity;
        this.prize = prize;
        this.drawTime = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Prize getPrize() {
        return prize;
    }

    public void setPrize(Prize prize) {
        this.prize = prize;
    }

    public LocalDateTime getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(LocalDateTime drawTime) {
        this.drawTime = drawTime;
    }
} 