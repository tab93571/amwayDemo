package com.example.demoproject.luckydraw.service;

import com.example.demoproject.luckydraw.dto.*;
import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.luckydraw.util.PrizeSelectionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for DrawExecutionService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DrawExecutionService Tests")
class DrawExecutionServiceTest {

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private DrawRecordRepository drawRecordRepository;

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private DrawValidationService drawValidationService;

    @Mock
    private UserActivityService userActivityService;

    @Mock
    private PrizeSelectionUtil prizeSelectionUtil;

    @InjectMocks
    private DrawExecutionService drawExecutionService;

    private User testUser;
    private Activity testActivity;
    private Prize testPrize1;
    private Prize testPrize2;
    private List<Prize> testPrizes;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        // Setup test activity
        testActivity = new Activity("Test Activity", "Test Description", 5);
        testActivity.setId(1L);

        // Setup test prizes
        testPrize1 = new Prize("iPhone 15", "Latest iPhone", 10, new BigDecimal("0.3"), testActivity);
        testPrize1.setId(1L);

        testPrize2 = new Prize("AirPods", "Wireless Earbuds", 5, new BigDecimal("0.2"), testActivity);
        testPrize2.setId(2L);

        testPrizes = Arrays.asList(testPrize1, testPrize2);
    }

    @Nested
    @DisplayName("Single Draw Tests")
    class SingleDrawTests {

        @Test
        @DisplayName("Should perform single draw successfully with prize")
        void shouldPerformSingleDrawSuccessfullyWithPrize() {
            // Given
            DrawRequest request = new DrawRequest(1L);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(testPrizes);
            when(prizeSelectionUtil.selectPrize(testPrizes)).thenReturn(testPrize1);
            when(drawRecordRepository.save(any(DrawRecord.class))).thenAnswer(invocation -> {
                DrawRecord record = invocation.getArgument(0);
                record.setId(1L);
                record.setDrawTime(LocalDateTime.now());
                return record;
            });
            when(prizeRepository.findByIdWithLock(1L)).thenReturn(testPrize1);
            when(prizeRepository.save(any(Prize.class))).thenReturn(testPrize1);

            // When
            DrawResult result = drawExecutionService.performDraw(request);

            // Then
            assertNotNull(result);
            assertEquals(testPrize1.getId(), result.getPrizeId());
            assertEquals(testPrize1.getName(), result.getPrizeName());
            assertEquals(testPrize1.getDescription(), result.getPrizeDescription());

            verify(drawValidationService).findActivity(1L);
            verify(drawValidationService).validateUserMultipleDrawLimit(1L, 1, 5);
            verify(drawValidationService).getAvailablePrizes(testActivity);
            verify(prizeSelectionUtil).selectPrize(testPrizes);
            verify(drawRecordRepository).save(any(DrawRecord.class));
            verify(prizeRepository).findByIdWithLock(1L);
            verify(prizeRepository).save(any(Prize.class));
        }

        @Test
        @DisplayName("Should return thank you message when no prize selected")
        void shouldReturnThankYouMessageWhenNoPrizeSelected() {
            // Given
            DrawRequest request = new DrawRequest(1L);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(testPrizes);
            when(prizeSelectionUtil.selectPrize(testPrizes)).thenReturn(null);

            DrawResult result = drawExecutionService.performDraw(request);

            assertNotNull(result);
            assertNull(result.getPrizeId());
            assertEquals("Thank You", result.getPrizeName());
            assertEquals("Better luck next time!", result.getPrizeDescription());

            verify(prizeSelectionUtil).selectPrize(testPrizes);
            verify(drawRecordRepository).save(any(DrawRecord.class));
            verify(prizeRepository, never()).findByIdWithLock(any());
            verify(prizeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            DrawRequest request = new DrawRequest(1L);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.empty());

            // When & Then
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                drawExecutionService.performDraw(request);
            });
            assertEquals("User not found with ID: 1", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Multiple Draw Tests")
    class MultipleDrawTests {

        @Test
        @DisplayName("Should perform multiple draws successfully with prizes")
        void shouldPerformMultipleDrawsSuccessfullyWithPrizes() {
            //
            MultipleDrawRequest request = new MultipleDrawRequest(1L, 3);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(testPrizes);
            when(prizeSelectionUtil.selectPrize(testPrizes))
                .thenReturn(testPrize1)  // First draw
                .thenReturn(testPrize2)  // Second draw
                .thenReturn(null);  // Third draw - no prize

            when(prizeRepository.findByIdWithLock(1L)).thenReturn(testPrize1);
            when(prizeRepository.findByIdWithLock(2L)).thenReturn(testPrize2);
            when(prizeRepository.save(any(Prize.class)))
                    .thenReturn(testPrize1)
                    .thenReturn(testPrize2);

            MultipleDrawResult result = drawExecutionService.performMultipleDraws(request);
            assertNotNull(result);
            assertEquals(3, result.getResults().size());

            DrawResult firstResult = result.getResults().get(0);
            assertEquals(testPrize1.getId(), firstResult.getPrizeId());
            assertEquals(testPrize1.getName(), firstResult.getPrizeName());

            DrawResult secondResult = result.getResults().get(1);
            assertEquals(testPrize2.getId(), secondResult.getPrizeId());
            assertEquals(testPrize2.getName(), secondResult.getPrizeName());

            DrawResult thirdResult = result.getResults().get(2);
            assertNull(thirdResult.getPrizeId());
            assertEquals("Thank You", thirdResult.getPrizeName());

            verify(prizeSelectionUtil, times(3)).selectPrize(testPrizes);
            verify(drawRecordRepository, times(3)).save(any(DrawRecord.class));
            verify(prizeRepository, times(2)).findByIdWithLock(any());
            verify(prizeRepository, times(2)).save(any());
        }

        @Test
        @DisplayName("Should handle all draws with no prizes")
        void shouldHandleAllDrawsWithNoPrizes() {
            // Given
            MultipleDrawRequest request = new MultipleDrawRequest(1L, 2);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(testPrizes);
            when(prizeSelectionUtil.selectPrize(testPrizes))
                .thenReturn(null)  // First draw
                .thenReturn(null); // Second draw

            MultipleDrawResult result = drawExecutionService.performMultipleDraws(request);

            assertNotNull(result);
            assertEquals(2, result.getResults().size());

            for (DrawResult drawResult : result.getResults()) {
                assertNull(drawResult.getPrizeId());
                assertEquals("Thank You", drawResult.getPrizeName());
                assertEquals("Better luck next time!", drawResult.getPrizeDescription());
            }

            verify(prizeSelectionUtil, times(2)).selectPrize(testPrizes);
            verify(drawRecordRepository, times(2)).save(any(DrawRecord.class));
            verify(prizeRepository, never()).findByIdWithLock(any());
            verify(prizeRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should stop on first error in multiple draws")
        void shouldStopOnFirstErrorInMultipleDraws() {
            // Given
            MultipleDrawRequest request = new MultipleDrawRequest(1L, 3);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(testPrizes);
            when(prizeSelectionUtil.selectPrize(testPrizes))
                    .thenReturn(testPrize1)
                    .thenReturn(testPrize2);

            when(prizeRepository.findByIdWithLock(1L)).thenReturn(testPrize1);
            when(prizeRepository.save(any(Prize.class)))
                    .thenReturn(testPrize1)                       // first call
                    .thenThrow(new IllegalStateException("DB error")); // second call

            MultipleDrawResult result = drawExecutionService.performMultipleDraws(request);

            assertNotNull(result);
            assertEquals(1, result.getResults().size()); // Should stop on error and return empty list
        }
    }

    @Nested
    @DisplayName("Inventory Management Tests")
    class InventoryManagementTests {

        @Test
        @DisplayName("Show thank you when prize inventory is zero")
        void shouldThrowExceptionWhenPrizeInventoryIsZero() {
            // Given
            Prize zeroQuantityPrize = new Prize("Test Prize", "Test Description", 0, new BigDecimal("0.5"), testActivity);
            zeroQuantityPrize.setId(1L);
            
            MultipleDrawRequest request = new MultipleDrawRequest(1L, 1);
            when(userActivityService.getCurrentUserId()).thenReturn(1L);
            when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(drawValidationService.findActivity(1L)).thenReturn(testActivity);
            when(drawValidationService.getAvailablePrizes(testActivity)).thenReturn(Arrays.asList(zeroQuantityPrize));
            when(prizeSelectionUtil.selectPrize(Arrays.asList(zeroQuantityPrize))).thenReturn(zeroQuantityPrize);

            when(prizeRepository.findByIdWithLock(1L)).thenReturn(zeroQuantityPrize);


            MultipleDrawResult result = drawExecutionService.performMultipleDraws(request);


            for (DrawResult drawResult : result.getResults()) {
                assertNull(drawResult.getPrizeId());
                assertEquals("Thank You", drawResult.getPrizeName());
                assertEquals("Better luck next time!", drawResult.getPrizeDescription());
            }
        }
    }
}
