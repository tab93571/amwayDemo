package com.example.demoproject.admin.service;

import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.luckydraw.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for admin draw history operations
 */
@Service
public class AdminDrawHistoryService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private DrawRecordRepository drawRecordRepository;

    /**
     * Get draw history for a specific activity
     * @param activityId Activity identifier
     * @return User draw history
     */
    public UserDrawHistory getDrawHistoryForActivity(Long activityId) {
        try {
            Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));

            List<DrawRecord> records = drawRecordRepository.findByActivityOrderByDrawTimeDesc(activity);
            
            return new UserDrawHistory(activityId, records);
        } catch (Exception e) {
            System.err.println("Error in getDrawHistoryForActivity: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
} 