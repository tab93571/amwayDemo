package com.example.demoproject.admin.service;

import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.luckydraw.dto.UserDrawHistory;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.DrawRecord;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.DrawRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminDrawHistoryServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private DrawRecordRepository drawRecordRepository;

    @InjectMocks
    private AdminDrawHistoryService adminDrawHistoryService;

    private User testUser;
    private Activity testActivity;
    private Prize testPrize;
    private DrawRecord testDrawRecord;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testActivity = new Activity("Test Activity", "Test Description", 10);
        testActivity.setId(1L);

        testPrize = new Prize();
        testPrize.setId(1L);
        testPrize.setName("Test Prize");
        testPrize.setDescription("Test Prize Description");

        testDrawRecord = new DrawRecord();
        testDrawRecord.setId(1L);
        testDrawRecord.setUser(testUser);
        testDrawRecord.setActivity(testActivity);
        testDrawRecord.setPrize(testPrize);
        testDrawRecord.setDrawTime(LocalDateTime.now());
    }

    @Test
    void getDrawHistoryForActivityShouldReturnDrawHistory() {

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.findByActivityOrderByDrawTimeDesc(testActivity))
            .thenReturn(Arrays.asList(testDrawRecord));

        UserDrawHistory result = adminDrawHistoryService.getDrawHistoryForActivity(1L);

        assertNotNull(result);
        assertEquals(1L, result.getActivityId());
        assertEquals(1, result.getHistory().size());
        
        UserDrawHistory.DrawHistoryItem historyItem = result.getHistory().get(0);
        assertEquals(1L, historyItem.getRecordId());
        assertEquals("testuser", historyItem.getUsername());
        assertEquals("Test Prize", historyItem.getPrizeName());
        assertEquals("Test Prize Description", historyItem.getPrizeDescription());
        assertNotNull(historyItem.getDrawTime());
        
        verify(activityRepository).findById(1L);
        verify(drawRecordRepository).findByActivityOrderByDrawTimeDesc(testActivity);
    }

    @Test
    void getDrawHistoryForActivityWhenActivityNotFoundShouldThrowException() {

        when(activityRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> adminDrawHistoryService.getDrawHistoryForActivity(1L));
        
        assertEquals("Activity not found: 1", exception.getMessage());
        verify(activityRepository).findById(1L);
        verify(drawRecordRepository, never()).findByActivityOrderByDrawTimeDesc(any());
    }

    @Test
    void getDrawHistoryForActivityWhenNoDrawRecordsShouldReturnEmptyHistory() {
        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.findByActivityOrderByDrawTimeDesc(testActivity))
            .thenReturn(Arrays.asList());

        UserDrawHistory result = adminDrawHistoryService.getDrawHistoryForActivity(1L);

        assertNotNull(result);
        assertEquals(1L, result.getActivityId());
        assertTrue(result.getHistory().isEmpty());
        
        verify(activityRepository).findById(1L);
        verify(drawRecordRepository).findByActivityOrderByDrawTimeDesc(testActivity);
    }

    @Test
    void getDrawHistoryForActivityWhenDrawRecordHasNoPrizeShouldHandleCorrectly() {
        // Arrange
        DrawRecord noPrizeRecord = new DrawRecord();
        noPrizeRecord.setId(2L);
        noPrizeRecord.setUser(testUser);
        noPrizeRecord.setActivity(testActivity);
        noPrizeRecord.setPrize(null);
        noPrizeRecord.setDrawTime(LocalDateTime.now());

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.findByActivityOrderByDrawTimeDesc(testActivity))
            .thenReturn(Arrays.asList(noPrizeRecord));

        UserDrawHistory result = adminDrawHistoryService.getDrawHistoryForActivity(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getActivityId());
        assertEquals(1, result.getHistory().size());
        
        UserDrawHistory.DrawHistoryItem historyItem = result.getHistory().get(0);
        assertEquals(2L, historyItem.getRecordId());
        assertEquals("testuser", historyItem.getUsername());
        assertEquals("沒有中獎", historyItem.getPrizeName());
        assertEquals("沒有中獎", historyItem.getPrizeDescription());
        
        verify(activityRepository).findById(1L);
        verify(drawRecordRepository).findByActivityOrderByDrawTimeDesc(testActivity);
    }

    @Test
    void getDrawHistoryForActivityWhenExceptionOccursShouldPropagateException() {
        // Arrange
        when(activityRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> adminDrawHistoryService.getDrawHistoryForActivity(1L));
        
        assertEquals("Database error", exception.getMessage());
        verify(activityRepository).findById(1L);
        verify(drawRecordRepository, never()).findByActivityOrderByDrawTimeDesc(any());
    }
}