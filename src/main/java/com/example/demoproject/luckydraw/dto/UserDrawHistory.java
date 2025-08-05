package com.example.demoproject.luckydraw.dto;

import com.example.demoproject.luckydraw.entity.DrawRecord;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO for user draw history
 */
public class UserDrawHistory {

    private final Long activityId;
    private final List<DrawHistoryItem> history;

    public UserDrawHistory(Long activityId, List<DrawRecord> records) {
        this.activityId = activityId;
        this.history = records.stream()
            .map(DrawHistoryItem::new)
            .collect(Collectors.toList());
    }

    public Long getActivityId() {
        return activityId;
    }

    public List<DrawHistoryItem> getHistory() {
        return history;
    }

    /**
     * Inner class for draw history item
     */
    public static class DrawHistoryItem {
        private final Long recordId;
        private final String username;
        private final String prizeName;
        private final String prizeDescription;
        private final LocalDateTime drawTime;

        public DrawHistoryItem(DrawRecord record) {
            this.recordId = record.getId();
            this.username = record.getUser().getUsername();
            this.prizeName = record.getPrize() == null ? "沒有中獎" : record.getPrize().getName();
            this.prizeDescription = record.getPrize() == null ? "沒有中獎" : record.getPrize().getDescription();
            this.drawTime = record.getDrawTime();
        }

        public Long getRecordId() {
            return recordId;
        }

        public String getUsername() {
            return username;
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
} 