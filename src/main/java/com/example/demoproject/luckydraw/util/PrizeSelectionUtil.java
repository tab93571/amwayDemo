package com.example.demoproject.luckydraw.util;

import com.example.demoproject.luckydraw.entity.Prize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Utility class for prize selection based on probability distribution
 */
@Component
public class PrizeSelectionUtil {

    /**
     * Select a prize from the list based on probability distribution
     * Uses cumulative probability method for fair selection
     * 
     * @param prizes List of available prizes
     * @return Selected prize or null if no prize is available
     */
    public Prize selectPrize(List<Prize> prizes) {
        if (prizes == null || prizes.isEmpty()) {
            return null;
        }

        // Generate random number between 0 and 1
        double random = ThreadLocalRandom.current().nextDouble();
        BigDecimal randomBigDecimal = BigDecimal.valueOf(random);

        // Calculate cumulative probability and select prize
        BigDecimal cumulativeProbability = BigDecimal.ZERO;
        for (Prize prize : prizes) {
            cumulativeProbability = cumulativeProbability.add(prize.getProbability());
            if (randomBigDecimal.compareTo(cumulativeProbability) <= 0) {
                // Check if prize is still available
                if (prize.getQuantity() > 0) {
                    return prize;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Select a prize with custom random number (useful for testing)
     * 
     * @param prizes List of available prizes
     * @param randomValue Custom random value between 0 and 1
     * @return Selected prize or null if no prize is available
     */
    public Prize selectPrize(List<Prize> prizes, double randomValue) {
        if (prizes == null || prizes.isEmpty()) {
            return null;
        }

        if (randomValue < 0.0 || randomValue > 1.0) {
            throw new IllegalArgumentException("Random value must be between 0.0 and 1.0");
        }

        BigDecimal randomBigDecimal = BigDecimal.valueOf(randomValue);
        BigDecimal cumulativeProbability = BigDecimal.ZERO;
        
        for (Prize prize : prizes) {
            cumulativeProbability = cumulativeProbability.add(prize.getProbability());
            if (randomBigDecimal.compareTo(cumulativeProbability) <= 0) {
                if (prize.getQuantity() > 0) {
                    return prize;
                } else {
                    return null;
                }
            }
        }
        
        return null;
    }
} 