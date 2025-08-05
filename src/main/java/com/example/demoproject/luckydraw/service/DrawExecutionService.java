package com.example.demoproject.luckydraw.service;

import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.luckydraw.dto.*;
import com.example.demoproject.luckydraw.constants.ErrorConstants;
import com.example.demoproject.luckydraw.exception.LuckyDrawException;
import com.example.demoproject.luckydraw.util.PrizeSelectionUtil;
import jakarta.persistence.LockTimeoutException;
import jakarta.persistence.PessimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.ArrayList;

/**
 * Service for executing draw operations
 */
@Service
public class DrawExecutionService {

    @Autowired
    private PrizeRepository prizeRepository;
    
    @Autowired
    private DrawRecordRepository drawRecordRepository;

    @Autowired
    private AuthUserRepository authUserRepository;

    @Autowired
    private DrawValidationService drawValidationService;

    @Autowired
    private UserActivityService userActivityService;

    @Autowired
    private PrizeSelectionUtil prizeSelectionUtil;

    /**
     * Perform a single draw
     * @param request Draw request
     * @return Draw result
     */
    @Transactional
    public DrawResult performDraw(DrawRequest request) {
        MultipleDrawRequest multipleRequest = new MultipleDrawRequest(request.getActivityId(), 1);
        MultipleDrawResult result = performMultipleDraws(multipleRequest);
        return result.getResults().get(0);
    }

    /**
     * Perform multiple draws (or single draw with count=1)
     * @param request Multiple draw request
     * @return Multiple draw results
     */
    @Transactional
    public MultipleDrawResult performMultipleDraws(MultipleDrawRequest request) {
        List<DrawResult> results = new ArrayList<>();

        Long userId = userActivityService.getCurrentUserId();
        User user = authUserRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Activity activity = drawValidationService.findActivity(request.getActivityId());
        drawValidationService.validateUserMultipleDrawLimit(request.getActivityId(), request.getDrawCount(), activity.getMaxDraws());

        List<Prize> availablePrizes = drawValidationService.getAvailablePrizes(activity);

        for (int i = 0; i < request.getDrawCount(); i++) {
            DrawRecord record = new DrawRecord(
                    user,
                    activity,
                    null
            );
            try {
                Prize selectedPrize = prizeSelectionUtil.selectPrize(availablePrizes);
                record.setPrize(selectedPrize);
                if (selectedPrize == null) {
                    results.add(new DrawResult("Thank You", "Better luck next time!", record.getDrawTime()));
                }else{
                    updatePrizeInventory(selectedPrize);
                    results.add(new DrawResult(selectedPrize, record.getDrawTime()));
                }
            } catch (LuckyDrawException e) {
                results.add(new DrawResult("Thank You", "Better luck next time!", LocalDateTime.now()));
                record.setPrize(null);
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
            drawRecordRepository.save(record);
        }
        
        return new MultipleDrawResult(results);
    }

    /**
     * Update prize inventory
     */
    private void updatePrizeInventory(Prize prize) {

        try {
            Prize lockedPrize = prizeRepository.findByIdWithLock(prize.getId());
            if (lockedPrize.getQuantity() <= 0) {
                throw new LuckyDrawException(ErrorConstants.ErrorType.NO_PRIZES_AVAILABLE);
            }
            lockedPrize.setQuantity(lockedPrize.getQuantity() - 1);
            prizeRepository.save(lockedPrize);
        } catch (LockTimeoutException | PessimisticLockException e) {
            throw new LuckyDrawException(
                    ErrorConstants.ErrorType.SYSTEM_BUSY
            );
        }
    }
} 