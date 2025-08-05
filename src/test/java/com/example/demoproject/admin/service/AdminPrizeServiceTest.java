package com.example.demoproject.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.admin.dto.PrizeResponse;
import com.example.demoproject.admin.dto.UpdatePrizeRequest;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.PrizeRepository;
import com.example.demoproject.luckydraw.service.UserActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
class AdminPrizeServiceTest {

    @Mock
    private PrizeRepository prizeRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private AdminPrizeService adminPrizeService;

    private Activity testActivity;
    private Prize testPrize;
    private UpdatePrizeRequest updateRequest;

    @BeforeEach
    void setUp() {
        testActivity = new Activity();
        testActivity.setId(1L);
        testActivity.setName("Test Activity");

        testPrize = new Prize();
        testPrize.setId(1L);
        testPrize.setName("Original Prize");
        testPrize.setDescription("Original Description");
        testPrize.setQuantity(10);
        testPrize.setProbability(new BigDecimal("0.3"));
        testPrize.setActivity(testActivity);

        updateRequest = new UpdatePrizeRequest();
        updateRequest.setName("Updated Prize");
        updateRequest.setDescription("Updated Description");
        updateRequest.setQuantity(20);
        updateRequest.setProbability(new BigDecimal("0.4"));
    }

    @Test
    void updatePrizeSuccess() throws JsonProcessingException {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(prizeRepository.findById(1L)).thenReturn(Optional.of(testPrize));
        when(prizeRepository.save(any(Prize.class))).thenReturn(testPrize);
        when(userActivityService.getCurrentUsername()).thenReturn("admin");

        PrizeResponse result = adminPrizeService.updatePrize(1L, 1L, updateRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getQuantity(), result.getQuantity());
        assertEquals(updateRequest.getProbability(), result.getProbability());
        assertEquals(1L, result.getActivityId());

        verify(prizeRepository).findById(1L);
        verify(prizeRepository).save(any(Prize.class));
        verify(auditService).logAction(eq("admin"), eq("UPDATE_PRIZE"), eq("PRIZE"), eq(1L), any(), any());
    }

    @Test
    void updatePrizePrizeNotFound() {

        when(prizeRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> adminPrizeService.updatePrize(1L, 1L, updateRequest));
        assertEquals("Prize not found: 1", exception.getMessage());
    }

    @Test
    void updatePrizePrizeDoesNotBelongToActivity() {

        Activity differentActivity = new Activity();
        differentActivity.setId(2L);
        testPrize.setActivity(differentActivity);
        when(prizeRepository.findById(1L)).thenReturn(Optional.of(testPrize));


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> adminPrizeService.updatePrize(1L, 1L, updateRequest));
        assertEquals("Prize does not belong to activity: 1", exception.getMessage());
    }

    @Test
    void updatePrizeTotalProbabilityExceedsOne() {

        when(prizeRepository.findById(1L)).thenReturn(Optional.of(testPrize));
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));

        Prize otherPrize = new Prize();
        otherPrize.setId(2L);
        otherPrize.setProbability(new BigDecimal("0.8"));
        otherPrize.setActivity(testActivity);

        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList(testPrize, otherPrize));

        updateRequest.setProbability(new BigDecimal("0.3")); // This would make total 0.8 + 0.3 = 1.1

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> adminPrizeService.updatePrize(1L, 1L, updateRequest));
        assertTrue(exception.getMessage().contains("Total probability"));
        assertTrue(exception.getMessage().contains("exceeds 1.0"));
    }

    @Test
    void updatePrizeAuditServiceThrowsException() throws JsonProcessingException {

        when(prizeRepository.findById(1L)).thenReturn(Optional.of(testPrize));
        when(prizeRepository.save(any(Prize.class))).thenReturn(testPrize);
        when(userActivityService.getCurrentUsername()).thenReturn("admin");
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList(testPrize));
        doThrow(new RuntimeException("Audit error")).when(auditService).logAction(any(), any(), any(), any(), any(), any());

        PrizeResponse result = adminPrizeService.updatePrize(1L, 1L, updateRequest);

        assertNotNull(result);
        verify(prizeRepository).save(any(Prize.class));
    }

    @Test
    void getPrizesSuccess() {

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList(testPrize));

        List<PrizeResponse> results = adminPrizeService.getPrizes(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        PrizeResponse result = results.get(0);
        assertEquals(1L, result.getId());
        assertEquals(testPrize.getName(), result.getName());
        assertEquals(testPrize.getDescription(), result.getDescription());
        assertEquals(testPrize.getQuantity(), result.getQuantity());
        assertEquals(testPrize.getProbability(), result.getProbability());
        assertEquals(1L, result.getActivityId());
    }

    @Test
    void getPrizes_ActivityNotFound() {

        when(activityRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> adminPrizeService.getPrizes(1L));
        assertEquals("Activity not found: 1", exception.getMessage());
    }

    @Test
    void getPrizes_EmptyPrizeList() {

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList());

        List<PrizeResponse> results = adminPrizeService.getPrizes(1L);

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void getPrizes_MultiplePrizes() {

        Prize prize2 = new Prize();
        prize2.setId(2L);
        prize2.setName("Second Prize");
        prize2.setDescription("Second Description");
        prize2.setQuantity(5);
        prize2.setProbability(new BigDecimal("0.2"));
        prize2.setActivity(testActivity);

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(prizeRepository.findByActivity(testActivity)).thenReturn(Arrays.asList(testPrize, prize2));

        List<PrizeResponse> results = adminPrizeService.getPrizes(1L);

        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("Original Prize", results.get(0).getName());
        assertEquals("Second Prize", results.get(1).getName());
    }

    @Test
    void getPrizes_ExceptionHandling() {

        when(activityRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> adminPrizeService.getPrizes(1L));
        assertEquals("Database error", exception.getMessage());
    }
}