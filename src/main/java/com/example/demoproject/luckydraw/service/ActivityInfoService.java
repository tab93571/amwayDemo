package com.example.demoproject.luckydraw.service;

import com.example.demoproject.luckydraw.entity.*;
import com.example.demoproject.luckydraw.repository.*;
import com.example.demoproject.luckydraw.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * Service for activity information operations
 */
@Service
public class ActivityInfoService {

    @Autowired
    private ActivityRepository activityRepository;

    /**
     * Get all available activities for users
     * @return List of available activities
     */
    public List<ActivityInfo> getAvailableActivities() {
        List<Activity> activities = activityRepository.findAll();
        return activities.stream()
            .map(activity -> new ActivityInfo(activity, new ArrayList<>()))
            .collect(Collectors.toList());
    }
} 