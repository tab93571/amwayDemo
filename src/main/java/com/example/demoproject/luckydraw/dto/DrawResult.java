package com.example.demoproject.luckydraw.dto;

import com.example.demoproject.luckydraw.entity.Prize;
import java.time.LocalDateTime;

/**
 * Result DTO for draw operation
 */
public class DrawResult {
    private final Long prizeId;
    private final String prizeName;
    private final String prizeDescription;

    private final LocalDateTime drawTime;

    public DrawResult(Prize prize, LocalDateTime drawTime) {
        this.prizeId = prize.getId();
        this.prizeName = prize.getName();
        this.prizeDescription = prize.getDescription();
        this.drawTime = drawTime;
    }

    public DrawResult(String prizeName, String prizeDescription, LocalDateTime drawTime) {
        this.prizeId = null;
        this.prizeName = prizeName;
        this.prizeDescription = prizeDescription;
        this.drawTime = drawTime;
    }

    public Long getPrizeId() {
        return prizeId;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public String getPrizeDescription() {
        return prizeDescription;
    }



    public LocalDateTime getDrawTime() {
        return drawTime;
    }
} 