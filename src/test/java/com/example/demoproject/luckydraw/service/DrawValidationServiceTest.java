package com.example.demoproject.luckydraw.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.PrizeRepository;
import com.example.demoproject.luckydraw.repository.DrawRecordRepository;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.luckydraw.exception.LuckyDrawException;
import com.example.demoproject.luckydraw.constants.ErrorConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DrawValidationServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private DrawRecordRepository drawRecordRepository;

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private DrawValidationService drawValidationService;

    private Activity testActivity;
    private User testUser;
    private List<Prize> testPrizes;

    @BeforeEach
    void setUp() {
        testActivity = new Activity("Test Activity", "Test Description", 10);
        testActivity.setId(1L);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        Prize prize1 = new Prize();
        prize1.setId(1L);
        prize1.setName("First Prize");
        prize1.setDescription("First Prize Description");
        prize1.setQuantity(1);
        prize1.setProbability(new BigDecimal("0.1"));
        prize1.setActivity(testActivity);

        Prize prize2 = new Prize();
        prize2.setId(2L);
        prize2.setName("Second Prize");
        prize2.setDescription("Second Prize Description");
        prize2.setQuantity(5);
        prize2.setProbability(new BigDecimal("0.2"));
        prize2.setActivity(testActivity);

        testPrizes = Arrays.asList(prize1, prize2);
    }

    @Test
    void findActivityWhenActivityExistsShouldReturnActivity() {

        Long activityId = 1L;
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(testActivity));


        Activity result = drawValidationService.findActivity(activityId);

        assertNotNull(result);
        assertEquals(testActivity.getId(), result.getId());
        assertEquals(testActivity.getName(), result.getName());
        assertEquals(testActivity.getDescription(), result.getDescription());
        assertEquals(testActivity.getMaxDraws(), result.getMaxDraws());

        verify(activityRepository).findById(activityId);
    }

    @Test
    void findActivityWhenActivityNotFoundShouldThrowException() {

        Long activityId = 999L;
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> drawValidationService.findActivity(activityId));
        assertEquals("Activity not found", exception.getMessage());

        verify(activityRepository).findById(activityId);
    }

    @Test
    void validateUserMultipleDrawLimitWhenUserCanDrawMultipleShouldNotThrowException() {

        Long activityId = 1L;
        int drawCount = 3;
        Integer maxDraws = 10;
        Long userId = 1L;
        
        when(userActivityService.getCurrentUserId()).thenReturn(userId);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.countByUserAndActivity(testUser, testActivity)).thenReturn(5L);

        assertDoesNotThrow(() -> drawValidationService.validateUserMultipleDrawLimit(activityId, drawCount, maxDraws));

        verify(userActivityService).getCurrentUserId();
        verify(authUserRepository).findById(userId);
        verify(activityRepository).findById(activityId);
        verify(drawRecordRepository).countByUserAndActivity(testUser, testActivity);
    }

    @Test
    void validateUserMultipleDrawLimitWhenUserCannotDrawMultipleShouldThrowException() {

        Long activityId = 1L;
        int drawCount = 5;
        Integer maxDraws = 10;
        Long userId = 1L;
        
        when(userActivityService.getCurrentUserId()).thenReturn(userId);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.countByUserAndActivity(testUser, testActivity)).thenReturn(8L);

        LuckyDrawException exception = assertThrows(LuckyDrawException.class, 
            () -> drawValidationService.validateUserMultipleDrawLimit(activityId, drawCount, maxDraws));
        assertEquals(ErrorConstants.ErrorType.USER_MULTIPLE_DRAW_LIMIT_REACHED.getCode(), exception.getErrorCode());
        assertEquals(ErrorConstants.ErrorType.USER_MULTIPLE_DRAW_LIMIT_REACHED.getMessage(), exception.getMessage());

        verify(userActivityService).getCurrentUserId();
        verify(authUserRepository).findById(userId);
        verify(activityRepository).findById(activityId);
        verify(drawRecordRepository).countByUserAndActivity(testUser, testActivity);
    }

    @Test
    void validateUserMultipleDrawLimitWhenUserAtExactLimitShouldNotThrowException() {
        Long activityId = 1L;
        int drawCount = 2;
        Integer maxDraws = 10;
        Long userId = 1L;
        
        when(userActivityService.getCurrentUserId()).thenReturn(userId);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.countByUserAndActivity(testUser, testActivity)).thenReturn(8L);

        assertDoesNotThrow(() -> drawValidationService.validateUserMultipleDrawLimit(activityId, drawCount, maxDraws));

        verify(userActivityService).getCurrentUserId();
        verify(authUserRepository).findById(userId);
        verify(activityRepository).findById(activityId);
        verify(drawRecordRepository).countByUserAndActivity(testUser, testActivity);
    }

    @Test
    void getAvailablePrizesWhenPrizesExistShouldReturnPrizes() {

        when(prizeRepository.findByActivity(testActivity)).thenReturn(testPrizes);

        List<Prize> result = drawValidationService.getAvailablePrizes(testActivity);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testPrizes.get(0).getId(), result.get(0).getId());
        assertEquals(testPrizes.get(1).getId(), result.get(1).getId());

        verify(prizeRepository).findByActivity(testActivity);
    }

    @Test
    void getAvailablePrizesWhenNoPrizesExistShouldThrowException() {

        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList());

        LuckyDrawException exception = assertThrows(LuckyDrawException.class, 
            () -> drawValidationService.getAvailablePrizes(testActivity));
        assertEquals(ErrorConstants.ErrorType.NO_PRIZES_AVAILABLE.getCode(), exception.getErrorCode());
        assertEquals(ErrorConstants.ErrorType.NO_PRIZES_AVAILABLE.getMessage(), exception.getMessage());

        verify(prizeRepository).findByActivity(testActivity);
    }

    @Test
    void validateActivityWithNullActivityIdShouldThrowException() {

        assertThrows(IllegalArgumentException.class, 
            () -> drawValidationService.findActivity(null));
    }

    @Test
    void validateUserMultipleDrawLimitWithNullActivityIdShouldThrowException() {

        when(userActivityService.getCurrentUserId()).thenReturn(1L);
        when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(null)).thenThrow(IllegalArgumentException.class);

        assertThrows(IllegalArgumentException.class, 
            () -> drawValidationService.validateUserMultipleDrawLimit(null, 5, 10));
    }
}