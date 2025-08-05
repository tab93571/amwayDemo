package com.example.demoproject.luckydraw.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.DrawRecordRepository;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.example.demoproject.luckydraw.dto.UserActivityInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserActivityServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Mock
    private DrawRecordRepository drawRecordRepository;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserActivityService userActivityService;

    private User testUser;
    private Activity testActivity;
    private Map<String, Object> authDetails;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testActivity = new Activity("Test Activity", "Test Description", 10);
        testActivity.setId(1L);

        // Setup authentication details
        authDetails = new HashMap<>();
        authDetails.put("userId", 1L);

        // Setup SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getDetails()).thenReturn(authDetails);

        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void getCurrentUserIdWhenAuthenticationDetailsValidShouldReturnUserId() {
        Long expectedUserId = 1L;
        when(authentication.getDetails()).thenReturn(authDetails);

        Long result = userActivityService.getCurrentUserId();

        assertEquals(expectedUserId, result);
        verify(authentication).getDetails();
    }


    @Test
    void getCurrentUserIdWhenUserIdNotInDetailsShouldThrowException() {
        Map<String, Object> emptyDetails = new HashMap<>();
        when(authentication.getDetails()).thenReturn(emptyDetails);

        IllegalStateException exception = assertThrows(IllegalStateException.class, 
            () -> userActivityService.getCurrentUserId());
        assertEquals("User ID not found in authentication context", exception.getMessage());

        verify(authentication).getDetails();
    }

    @Test
    void getUserActivityInfoWhenUserAndActivityExistShouldReturnUserActivityInfo() {
        Long activityId = 1L;
        Long userId = 1L;
        int currentDraws = 5;
        
        when(authentication.getDetails()).thenReturn(authDetails);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(activityId)).thenReturn(Optional.of(testActivity));
        when(drawRecordRepository.countByUserAndActivity(testUser, testActivity)).thenReturn((long) currentDraws);

        UserActivityInfo result = userActivityService.getUserActivityInfo(activityId);

        assertNotNull(result);
        assertEquals(testUser.getUsername(), result.getUsername());
        assertEquals(activityId, result.getActivityId());
        assertEquals(testActivity.getMaxDraws(), result.getMaxDraws());
        assertEquals(currentDraws, result.getCurrentDraws());
        assertEquals(testActivity.getMaxDraws() - currentDraws, result.getRemainingDraws());

        verify(authentication).getDetails();
        verify(authUserRepository).findById(userId);
        verify(activityRepository).findById(activityId);
        verify(drawRecordRepository).countByUserAndActivity(testUser, testActivity);
    }

    @Test
    void getUserActivityInfoWhenUserNotFoundShouldThrowException() {
        Long activityId = 1L;
        Long userId = 999L;
        
        Map<String, Object> detailsWithInvalidUser = new HashMap<>();
        detailsWithInvalidUser.put("userId", userId);
        when(authentication.getDetails()).thenReturn(detailsWithInvalidUser);
        when(authUserRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> userActivityService.getUserActivityInfo(activityId));
        assertEquals("User not found with ID: " + userId, exception.getMessage());

        verify(authentication).getDetails();
        verify(authUserRepository).findById(userId);
        verify(activityRepository, never()).findById(any());
        verify(drawRecordRepository, never()).countByUserAndActivity(any(), any());
    }

    @Test
    void getUserActivityInfoWhenActivityNotFoundShouldThrowException() {
        Long activityId = 999L;
        Long userId = 1L;
        
        when(authentication.getDetails()).thenReturn(authDetails);
        when(authUserRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(activityRepository.findById(activityId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> userActivityService.getUserActivityInfo(activityId));
        assertEquals("Activity not found", exception.getMessage());

        verify(authentication).getDetails();
        verify(authUserRepository).findById(userId);
        verify(activityRepository).findById(activityId);
        verify(drawRecordRepository, never()).countByUserAndActivity(any(), any());
    }
}