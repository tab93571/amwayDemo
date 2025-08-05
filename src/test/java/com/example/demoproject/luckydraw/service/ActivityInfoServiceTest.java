package com.example.demoproject.luckydraw.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.luckydraw.entity.Activity;
import com.example.demoproject.luckydraw.entity.Prize;
import com.example.demoproject.luckydraw.repository.ActivityRepository;
import com.example.demoproject.luckydraw.repository.PrizeRepository;
import com.example.demoproject.luckydraw.dto.ActivityInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ActivityInfoServiceTest {

    @Mock
    private ActivityRepository activityRepository;
    @InjectMocks
    private ActivityInfoService activityInfoService;

    private Activity testActivity;
    private List<Prize> testPrizes;

    @BeforeEach
    void setUp() {
        testActivity = new Activity("Test Activity", "Test Description", 10);
        testActivity.setId(1L);

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
    void getAvailableActivitiesWhenActivitiesExist_ShouldReturnAllActivities() {
        // Arrange
        Activity activity1 = new Activity("Activity 1", "Description 1", 5);
        activity1.setId(1L);
        Activity activity2 = new Activity("Activity 2", "Description 2", 10);
        activity2.setId(2L);
        List<Activity> activities = Arrays.asList(activity1, activity2);

        when(activityRepository.findAll()).thenReturn(activities);

        // Act
        List<ActivityInfo> result = activityInfoService.getAvailableActivities();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        
        ActivityInfo firstActivity = result.get(0);
        assertEquals(activity1.getId(), firstActivity.getActivityId());
        assertEquals(activity1.getName(), firstActivity.getName());
        assertEquals(activity1.getDescription(), firstActivity.getDescription());
        assertEquals(activity1.getMaxDraws(), firstActivity.getMaxDraws());
        assertTrue(firstActivity.getPrizes().isEmpty()); // Empty prize list as per service logic

        ActivityInfo secondActivity = result.get(1);
        assertEquals(activity2.getId(), secondActivity.getActivityId());
        assertEquals(activity2.getName(), secondActivity.getName());
        assertEquals(activity2.getDescription(), secondActivity.getDescription());
        assertEquals(activity2.getMaxDraws(), secondActivity.getMaxDraws());
        assertTrue(secondActivity.getPrizes().isEmpty()); // Empty prize list as per service logic

        verify(activityRepository).findAll();
    }

    @Test
    void getAvailableActivitiesWhenNoActivitiesExistShouldReturnEmptyList() {
        // Arrange
        when(activityRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<ActivityInfo> result = activityInfoService.getAvailableActivities();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(activityRepository).findAll();
    }
}