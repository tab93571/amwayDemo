package com.example.demoproject.luckydraw.dto;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for activity information
 */
public class ActivityInfo {
    private final Long activityId;
    private final String name;
    private final String description;
    private final Integer maxDraws;
    private final List<PrizeInfo> prizes;

    public ActivityInfo(Activity activity, List<Prize> prizes) {
        this.activityId = activity.getId();
        this.name = activity.getName();
        this.description = activity.getDescription();
        this.maxDraws = activity.getMaxDraws();
        this.prizes = prizes.stream()
            .map(PrizeInfo::new)
            .collect(Collectors.toList());
    }

    public Long getActivityId() {
        return activityId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getMaxDraws() {
        return maxDraws;
    }

    public List<PrizeInfo> getPrizes() {
        return prizes;
    }

    /**
     * Inner class for prize information
     */
    public static class PrizeInfo {
        private final Long id;
        private final String name;
        private final String description;
        private final Integer quantity;
        private final String probability;


        public PrizeInfo(Prize prize) {
            this.id = prize.getId();
            this.name = prize.getName();
            this.description = prize.getDescription();
            this.quantity = prize.getQuantity();
            this.probability = prize.getProbability().toString();

        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public String getProbability() {
            return probability;
        }


    }
} 