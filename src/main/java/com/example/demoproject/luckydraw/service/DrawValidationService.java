package com.example.demoproject.luckydraw.service;

import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.luckydraw.constants.ErrorConstants;
import com.example.demoproject.luckydraw.exception.LuckyDrawException;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service for validating draw operations
 */
@Service
public class DrawValidationService {

    @Autowired
    private ActivityRepository activityRepository;
    
    @Autowired
    private PrizeRepository prizeRepository;
    
    @Autowired
    private DrawRecordRepository drawRecordRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private UserActivityService userActivityService;

    /**
     * Validate activity exists and is active
     */
    public Activity findActivity(Long activityId) {
        return activityRepository.findById(activityId)
            .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
    }

    /**
     * Validate user multiple draw limit (using Activity's maxDraws)
     */
    public void validateUserMultipleDrawLimit(Long activityId, int drawCount, Integer maxDraws) {

        Long userId = userActivityService.getCurrentUserId();
        User user = authUserRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));
        long userDrawCount = drawRecordRepository.countByUserAndActivity(user, activity);
        
        if (userDrawCount + drawCount > maxDraws) {
            throw new LuckyDrawException(ErrorConstants.ErrorType.USER_MULTIPLE_DRAW_LIMIT_REACHED);
        }
    }

    /**
     * Get available prizes for activity
     */
    public List<Prize> getAvailablePrizes(Activity activity) {
        List<Prize> prizes = prizeRepository.findByActivity(activity);
        if (prizes.isEmpty()) {
            throw new LuckyDrawException(ErrorConstants.ErrorType.NO_PRIZES_AVAILABLE);
        }
        return prizes;
    }
} 