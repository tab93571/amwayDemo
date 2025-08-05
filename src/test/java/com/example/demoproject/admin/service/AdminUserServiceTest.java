package com.example.demoproject.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.admin.dto.UserResponse;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private AuthUserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    private User testUser1;
    private User testUser2;

    @BeforeEach
    void setUp() {
        testUser1 = new User();
        testUser1.setId(1L);
        testUser1.setUsername("admin");
        testUser1.setRole("ADMIN");

        testUser2 = new User();
        testUser2.setId(2L);
        testUser2.setUsername("user");
        testUser2.setRole("USER");
    }

    @Test
    void getAllUsersSuccess() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser1, testUser2));

        // Act
        List<UserResponse> results = adminUserService.getAllUsers();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());

        UserResponse firstUser = results.get(0);
        assertEquals("1", firstUser.getUserId());
        assertEquals("admin", firstUser.getUsername());
        assertEquals("ADMIN", firstUser.getRole());
        assertNull(firstUser.getCreatedAt());
        assertNull(firstUser.getUpdatedAt());

        UserResponse secondUser = results.get(1);
        assertEquals("2", secondUser.getUserId());
        assertEquals("user", secondUser.getUsername());
        assertEquals("USER", secondUser.getRole());
        assertNull(secondUser.getCreatedAt());
        assertNull(secondUser.getUpdatedAt());

        verify(userRepository).findAll();
    }

    @Test
    void getAllUsersEmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<UserResponse> results = adminUserService.getAllUsers();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(userRepository).findAll();
    }
}