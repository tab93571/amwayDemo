package com.example.demoproject.admin.service;

import com.example.demoproject.admin.dto.ActivityResponse;
import com.example.demoproject.admin.dto.UpdateActivityRequest;
import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.service.UserActivityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminActivityServiceTest {

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private AuditService auditService;

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private AdminActivityService adminActivityService;

    private Activity testActivity;
    private UpdateActivityRequest updateRequest;

    @BeforeEach
    void setUp() {
        testActivity = new Activity("Test Activity", "Test Description", 10);
        testActivity.setId(1L);
        
        updateRequest = new UpdateActivityRequest("Updated Activity", "Updated Description", 15);
    }

    @Test
    void getAllActivitiesShouldReturnAllActivities() {

        Activity activity1 = new Activity("Activity 1", "Description 1", 5);
        activity1.setId(1L);
        Activity activity2 = new Activity("Activity 2", "Description 2", 10);
        activity2.setId(2L);
        
        List<Activity> activities = Arrays.asList(activity1, activity2);
        when(activityRepository.findAll()).thenReturn(activities);

        List<ActivityResponse> result = adminActivityService.getAllActivities();

        assertNotNull(result);
        assertEquals(2, result.size());
        
        ActivityResponse firstResponse = result.get(0);
        assertEquals("1", firstResponse.getActivityId());
        assertEquals("Activity 1", firstResponse.getName());
        assertEquals("Description 1", firstResponse.getDescription());
        assertEquals(5, firstResponse.getMaxDraws());
        
        ActivityResponse secondResponse = result.get(1);
        assertEquals("2", secondResponse.getActivityId());
        assertEquals("Activity 2", secondResponse.getName());
        assertEquals("Description 2", secondResponse.getDescription());
        assertEquals(10, secondResponse.getMaxDraws());
        
        verify(activityRepository).findAll();
    }


    @Test
    void updateActivityShouldUpdateActivitySuccessfully() throws JsonProcessingException {

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(activityRepository.save(any(Activity.class))).thenReturn(testActivity);
        when(userActivityService.getCurrentUsername()).thenReturn("admin");

        ActivityResponse result = adminActivityService.updateActivity("1", updateRequest);

        // Assert
        assertNotNull(result);
        assertEquals("1", result.getActivityId());
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getMaxDraws(), result.getMaxDraws());
        
        verify(activityRepository).findById(1L);
        verify(activityRepository).save(any(Activity.class));
        verify(userActivityService).getCurrentUsername();
        verify(auditService).logAction(eq("admin"), eq("UPDATE_ACTIVITY"), eq("ACTIVITY"), eq(1L), any(), any());
    }

    @Test
    void updateActivityWhenActivityNotFoundShouldThrowException() throws JsonProcessingException {
        // Arrange
        when(activityRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> adminActivityService.updateActivity("1", updateRequest));
        
        assertEquals("Activity not found: 1", exception.getMessage());
        verify(activityRepository).findById(1L);
        verify(activityRepository, never()).save(any());
        verify(auditService, never()).logAction(any(), any(), any(), any(), any(), any());
    }

    @Test
    void updateActivityWhenAuditLoggingFailsShouldStillUpdateActivity() throws JsonProcessingException {

        when(activityRepository.findById(1L)).thenReturn(Optional.of(testActivity));
        when(activityRepository.save(any(Activity.class))).thenReturn(testActivity);
        when(userActivityService.getCurrentUsername()).thenThrow(new RuntimeException("Audit service error"));


        ActivityResponse result = adminActivityService.updateActivity("1", updateRequest);

        assertNotNull(result);
        assertEquals("1", result.getActivityId());
        assertEquals(updateRequest.getName(), result.getName());
        assertEquals(updateRequest.getDescription(), result.getDescription());
        assertEquals(updateRequest.getMaxDraws(), result.getMaxDraws());
        
        verify(activityRepository).findById(1L);
        verify(activityRepository).save(any(Activity.class));
        verify(userActivityService).getCurrentUsername();
        verify(auditService, never()).logAction(any(), any(), any(), any(), any(), any());
    }
}