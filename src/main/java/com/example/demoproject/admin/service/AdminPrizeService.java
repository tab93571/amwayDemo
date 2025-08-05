package com.example.demoproject.admin.service;

import com.example.demoproject.admin.dto.PrizeResponse;
import com.example.demoproject.admin.dto.UpdatePrizeRequest;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.PrizeRepository;
import com.example.demoproject.luckydraw.service.UserActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminPrizeService {

    @Autowired
    private PrizeRepository prizeRepository;
    
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private AuditService auditService;

    @Autowired
    private UserActivityService userActivityService;

    public PrizeResponse updatePrize(Long activityId, Long prizeId, UpdatePrizeRequest request) {
        Prize prize = prizeRepository.findById(prizeId)
            .orElseThrow(() -> new IllegalArgumentException("Prize not found: " + prizeId));

        if (!prize.getActivity().getId().equals(activityId)) {
            throw new IllegalArgumentException("Prize does not belong to activity: " + activityId);
        }

        PrizeAuditData oldData = new PrizeAuditData(
            prize.getName(),
            prize.getDescription(),
            prize.getQuantity(),
            prize.getProbability()
        );

        // Validate total probability before updating
        validateTotalProbability(activityId, prizeId, request.getProbability());

        // Update prize details
        prize.setName(request.getName());
        prize.setQuantity(request.getQuantity());
        prize.setDescription(request.getDescription());
        prize.setProbability(request.getProbability());

        Prize savedPrize = prizeRepository.save(prize);

        PrizeAuditData newData = new PrizeAuditData(
            savedPrize.getName(),
            savedPrize.getDescription(),
            savedPrize.getQuantity(),
            savedPrize.getProbability()
        );

        // Log the audit
        try {
            String adminUsername = userActivityService.getCurrentUsername();
            auditService.logAction(adminUsername, "UPDATE_PRIZE", "PRIZE", prizeId, oldData, newData);
        } catch (Exception e) {
            // Log error but don't fail the update
            System.err.println("Error logging prize update audit: " + e.getMessage());
        }
        
        return new PrizeResponse(
            savedPrize.getId(),
            savedPrize.getName(),
            savedPrize.getDescription(),
            savedPrize.getQuantity(),
            savedPrize.getQuantity(), // remainingQuantity - same as quantity for now
            savedPrize.getProbability(),
            savedPrize.getActivity().getId(),
            null, // createdAt - not available in simplified entity
            null  // updatedAt - not available in simplified entity
        );
    }

    /**
     * Inner class for audit data
     */
    private static class PrizeAuditData {
        private final String name;
        private final String description;
        private final Integer quantity;
        private final BigDecimal probability;

        public PrizeAuditData(String name, String description, Integer quantity, BigDecimal probability) {
            this.name = name;
            this.description = description;
            this.quantity = quantity;
            this.probability = probability;
        }

        // Getters
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Integer getQuantity() { return quantity; }
        public BigDecimal getProbability() { return probability; }
    }

    public List<PrizeResponse> getPrizes(Long activityId) {
        try {
            Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));
            List<Prize> prizes = prizeRepository.findByActivity(activity);
            System.out.println("DEBUG: Found " + prizes.size() + " prizes for activity " + activityId);
            
            List<PrizeResponse> responses = prizes.stream()
                .map(prize -> {
                    System.out.println("DEBUG: Processing prize: " + prize.getName() + " (ID: " + prize.getId() + ")");
                    return new PrizeResponse(
                        prize.getId(),
                        prize.getName(),
                        prize.getDescription(),
                        prize.getQuantity(),
                        prize.getQuantity(), // remainingQuantity - same as quantity for now
                        prize.getProbability(),
                        prize.getActivity().getId(),
                        null, // createdAt - not available in simplified entity
                        null  // updatedAt - not available in simplified entity
                    );
                })
                .collect(Collectors.toList());
            
            System.out.println("DEBUG: Created " + responses.size() + " prize responses");
            return responses;
        } catch (Exception e) {
            System.err.println("ERROR in getPrizes: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Validate that the total probability of all prizes in an activity is ≤ 1.0
     * @param activityId The activity ID
     * @param currentPrizeId The current prize being updated (exclude from calculation)
     * @param newProbability The new probability for the current prize
     * @throws IllegalArgumentException if total probability exceeds 1.0
     */
    private void validateTotalProbability(Long activityId, Long currentPrizeId, BigDecimal newProbability) {
        Activity activity = activityRepository.findById(activityId)
            .orElseThrow(() -> new IllegalArgumentException("Activity not found: " + activityId));
        
        List<Prize> prizes = prizeRepository.findByActivity(activity);
        
        // Calculate total probability excluding the current prize being updated
        BigDecimal totalProbability = BigDecimal.ZERO;
        for (Prize prize : prizes) {
            if (!prize.getId().equals(currentPrizeId)) {
                totalProbability = totalProbability.add(prize.getProbability());
            }
        }
        
        // Add the new probability for the current prize
        totalProbability = totalProbability.add(newProbability);
        
        // Check if total exceeds 1.0
        if (totalProbability.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException(
                String.format("Total probability (%.4f) exceeds 1.0. " +
                    "Current total without this prize: %.4f, " +
                    "New prize probability: %.4f", 
                    totalProbability,
                    totalProbability.subtract(newProbability),
                    newProbability)
            );
        }
    }
} 