package com.example.demoproject.admin.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demoproject.admin.entity.AuditLog;
import com.example.demoproject.admin.repository.AuditLogRepository;
import com.example.demoproject.auth.entity.User;
import com.example.demoproject.auth.repository.AuthUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AuditServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private AuthUserRepository authUserRepository;

    @InjectMocks
    private AuditService auditService;

    private User testUser;
    private AuditLog testAuditLog;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("admin");
        testUser.setRole("ADMIN");

        testAuditLog = new AuditLog();
        testAuditLog.setId(1L);
        testAuditLog.setAdminUser(testUser);
        testAuditLog.setAction("UPDATE_PRIZE");
        testAuditLog.setEntityType("PRIZE");
        testAuditLog.setEntityId(1L);
        testAuditLog.setOldValues("{\"name\":\"Old Prize\"}");
        testAuditLog.setNewValues("{\"name\":\"New Prize\"}");
        testAuditLog.setTimestamp(LocalDateTime.now());
    }

    @Test
    void logActionSuccess() throws JsonProcessingException {
        // Arrange
        when(authUserRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        Object oldValues = new Object() {
            public String name = "Old Prize";
        };
        Object newValues = new Object() {
            public String name = "New Prize";
        };

        // Act
        auditService.logAction("admin", "UPDATE_PRIZE", "PRIZE", 1L, oldValues, newValues);

        // Assert
        verify(authUserRepository).findByUsername("admin");
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void logActionAdminUserNotFound() {

        when(authUserRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> 
            auditService.logAction("nonexistent", "UPDATE_PRIZE", "PRIZE", 1L, null, null));
        
        verify(authUserRepository).findByUsername("nonexistent");
        verify(auditLogRepository, never()).save(any(AuditLog.class));
    }

    @Test
    void logActionWithNullValues() throws JsonProcessingException {
        // Arrange
        when(authUserRepository.findByUsername("admin")).thenReturn(Optional.of(testUser));
        when(auditLogRepository.save(any(AuditLog.class))).thenReturn(testAuditLog);

        // Act
        auditService.logAction("admin", "DELETE_PRIZE", "PRIZE", 1L, null, null);

        // Assert
        verify(authUserRepository).findByUsername("admin");
        verify(auditLogRepository).save(any(AuditLog.class));
    }

    @Test
    void getAllAuditLogsSuccess() {
        // Arrange
        AuditLog auditLog2 = new AuditLog();
        auditLog2.setId(2L);
        auditLog2.setAdminUser(testUser);
        auditLog2.setAction("CREATE_ACTIVITY");
        auditLog2.setEntityType("ACTIVITY");
        auditLog2.setEntityId(2L);

        when(auditLogRepository.findAll()).thenReturn(Arrays.asList(testAuditLog, auditLog2));

        // Act
        List<AuditLog> results = auditService.getAllAuditLogs();

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("UPDATE_PRIZE", results.get(0).getAction());
        assertEquals("CREATE_ACTIVITY", results.get(1).getAction());
        verify(auditLogRepository).findAll();
    }

    @Test
    void getAllAuditLogsEmptyList() {
        // Arrange
        when(auditLogRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<AuditLog> results = auditService.getAllAuditLogs();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(auditLogRepository).findAll();
    }

    @Test
    void getAllAuditLogsRepositoryThrowsException() {
        // Arrange
        when(auditLogRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> auditService.getAllAuditLogs());
        assertEquals("Database error", exception.getMessage());
        verify(auditLogRepository).findAll();
    }

    @Test
    void getAuditLogsByEntityTypeSuccess() {
        // Arrange
        AuditLog prizeAuditLog = new AuditLog();
        prizeAuditLog.setId(1L);
        prizeAuditLog.setEntityType("PRIZE");
        prizeAuditLog.setAction("UPDATE_PRIZE");

        AuditLog activityAuditLog = new AuditLog();
        activityAuditLog.setId(2L);
        activityAuditLog.setEntityType("ACTIVITY");
        activityAuditLog.setAction("CREATE_ACTIVITY");

        when(auditLogRepository.findByEntityTypeOrderByTimestampDesc("PRIZE"))
            .thenReturn(Arrays.asList(prizeAuditLog));

        // Act
        List<AuditLog> results = auditService.getAuditLogsByEntityType("PRIZE");

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("PRIZE", results.get(0).getEntityType());
        assertEquals("UPDATE_PRIZE", results.get(0).getAction());
        verify(auditLogRepository).findByEntityTypeOrderByTimestampDesc("PRIZE");
    }

    @Test
    void getAuditLogsByEntityTypeEmptyList() {
        // Arrange
        when(auditLogRepository.findByEntityTypeOrderByTimestampDesc("NONEXISTENT"))
            .thenReturn(Arrays.asList());

        // Act
        List<AuditLog> results = auditService.getAuditLogsByEntityType("NONEXISTENT");

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(auditLogRepository).findByEntityTypeOrderByTimestampDesc("NONEXISTENT");
    }

    @Test
    void getAuditLogsByEntityTypeMultipleLogs() {
        // Arrange
        AuditLog auditLog1 = new AuditLog();
        auditLog1.setId(1L);
        auditLog1.setEntityType("PRIZE");
        auditLog1.setAction("CREATE_PRIZE");

        AuditLog auditLog2 = new AuditLog();
        auditLog2.setId(2L);
        auditLog2.setEntityType("PRIZE");
        auditLog2.setAction("UPDATE_PRIZE");

        when(auditLogRepository.findByEntityTypeOrderByTimestampDesc("PRIZE"))
            .thenReturn(Arrays.asList(auditLog1, auditLog2));

        // Act
        List<AuditLog> results = auditService.getAuditLogsByEntityType("PRIZE");

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("PRIZE", results.get(0).getEntityType());
        assertEquals("PRIZE", results.get(1).getEntityType());
        verify(auditLogRepository).findByEntityTypeOrderByTimestampDesc("PRIZE");
    }

    @Test
    void getAuditLogsByEntityTypeRepositoryThrowsException() {
        // Arrange
        when(auditLogRepository.findByEntityTypeOrderByTimestampDesc("PRIZE"))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> auditService.getAuditLogsByEntityType("PRIZE"));
        assertEquals("Database error", exception.getMessage());
        verify(auditLogRepository).findByEntityTypeOrderByTimestampDesc("PRIZE");
    }
}