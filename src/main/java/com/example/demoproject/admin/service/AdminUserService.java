package com.example.demoproject.admin.service;

import com.example.demoproject.admin.dto.CreateUserRequest;
import com.example.demoproject.admin.dto.UserResponse;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    @Autowired
    private AuthUserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
            .map(user -> new UserResponse(
                user.getId().toString(),
                user.getUsername(),
                user.getRole(),
                null, // createdAt - not available in simplified User entity
                null  // updatedAt - not available in simplified User entity
            ))
            .collect(Collectors.toList());
    }
} 