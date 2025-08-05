package com.example.demoproject.luckydraw.util;

import static org.junit.jupiter.api.Assertions.*;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class PrizeSelectionUtilTest {

    private PrizeSelectionUtil prizeSelectionUtil;
    private Activity testActivity;
    private Prize prize1;
    private Prize prize2;
    private Prize prize3;

    @BeforeEach
    void setUp() {
        prizeSelectionUtil = new PrizeSelectionUtil();
        
        testActivity = new Activity();
        testActivity.setId(1L);
        testActivity.setName("Test Activity");

        prize1 = new Prize();
        prize1.setId(1L);
        prize1.setName("High Value Prize");
        prize1.setDescription("Rare prize with low probability");
        prize1.setQuantity(5);
        prize1.setProbability(new BigDecimal("0.1"));
        prize1.setActivity(testActivity);

        prize2 = new Prize();
        prize2.setId(2L);
        prize2.setName("Medium Value Prize");
        prize2.setDescription("Common prize with medium probability");
        prize2.setQuantity(20);
        prize2.setProbability(new BigDecimal("0.3"));
        prize2.setActivity(testActivity);

        prize3 = new Prize();
        prize3.setId(3L);
        prize3.setName("Low Value Prize");
        prize3.setDescription("Common prize with high probability");
        prize3.setQuantity(50);
        prize3.setProbability(new BigDecimal("0.5"));
        prize3.setActivity(testActivity);
    }

    @Test
    void selectPrizeEmptyList() {

        Prize result = prizeSelectionUtil.selectPrize(Collections.emptyList());

        assertNull(result);
    }


    @Test
    void selectPrizeSinglePrizeWithZeroQuantity() {
        // Arrange
        prize1.setQuantity(0);
        prize1.setProbability(new BigDecimal("1"));
        List<Prize> prizes = Arrays.asList(prize1);

        Prize result = prizeSelectionUtil.selectPrize(prizes);

        assertNull(result);
    }

    @Test
    void selectPrizeWithCustomRandomValue() {
        List<Prize> prizes = Arrays.asList(prize1, prize2, prize3);
        double randomValue = 0.2;

        Prize result = prizeSelectionUtil.selectPrize(prizes, randomValue);

        assertNotNull(result);
        assertEquals(prize2.getId(), result.getId());
    }

    @Test
    void selectPrizeWithCustomRandomValueSelectsFirstPrize() {
        // Arrange
        List<Prize> prizes = Arrays.asList(prize1, prize2, prize3);
        double randomValue = 0.05; // Should select prize1 (cumulative probability 0.1)

        // Act
        Prize result = prizeSelectionUtil.selectPrize(prizes, randomValue);

        // Assert
        assertNotNull(result);
        assertEquals(prize1.getId(), result.getId());
    }

    @Test
    void selectPrizeWithCustomRandomValueSelectsLastPrize() {
        // Arrange
        List<Prize> prizes = Arrays.asList(prize1, prize2, prize3);
        double randomValue = 0.8; // Should select prize3 (cumulative probability 0.9)

        // Act
        Prize result = prizeSelectionUtil.selectPrize(prizes, randomValue);

        // Assert
        assertNotNull(result);
        assertEquals(prize3.getId(), result.getId());
    }

    @Test
    void selectPrizeWithCustomRandomValueNoPrizeSelected() {
        List<Prize> prizes = Arrays.asList(prize1, prize2, prize3);
        double randomValue = 0.95; // Should not select any prize (total probability 0.9)

        Prize result = prizeSelectionUtil.selectPrize(prizes, randomValue);

        assertNull(result);
    }

    @Test
    void selectPrizeProbabilityEdgeCases() {
        // Arrange
        Prize edgePrize = new Prize();
        edgePrize.setId(4L);
        edgePrize.setName("Edge Prize");
        edgePrize.setQuantity(1);
        edgePrize.setProbability(new BigDecimal("1.0"));
        edgePrize.setActivity(testActivity);

        List<Prize> prizes = Arrays.asList(edgePrize);

        Prize result = prizeSelectionUtil.selectPrize(prizes, 0.9999);

        assertNotNull(result);
        assertEquals(edgePrize.getId(), result.getId());
    }
} 