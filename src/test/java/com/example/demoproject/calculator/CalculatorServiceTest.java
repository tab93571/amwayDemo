package com.example.demoproject.calculator;

import com.example.demoproject.calculator.dto.StackSizesResponse;
import com.example.demoproject.calculator.validation.CalculatorValidationService;
import com.example.demoproject.calculator.CalculatorService.CalculationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import com.example.demoproject.calculator.constants.CalculatorConstants;

/**
 * Comprehensive unit tests for CalculatorService
 * 全面的單元測試，涵蓋正常情境與邊界／錯誤案例
 */
@ExtendWith(MockitoExtension.class)
class CalculatorServiceTest {

    @Mock
    private CalculatorValidationService validationService;

    @InjectMocks
    private CalculatorService calculatorService;

    private String testSessionId;

    
    @BeforeEach
    void setUp() {
        testSessionId = "test_session_123";
        // Initialize session
        calculatorService.initSession();
    }

    // ==================== Basic Operations ====================

    @Test
    void testAddition() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("5.5"), 
            new BigDecimal("3.2"), 
            "+"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("8.700000"), result.getResult());
        assertEquals(1, result.getStackSizes().getUndoCount());
        assertEquals(0, result.getStackSizes().getRedoCount());
    }

    @Test
    void testSubtraction() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10.5"), 
            new BigDecimal("3.2"), 
            "-"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("7.300000"), result.getResult());
    }

    @Test
    void testMultiplication() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("2.5"), 
            new BigDecimal("3.0"), 
            "*"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("7.500000"), result.getResult());
    }

    @Test
    void testDivision() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10.0"), 
            new BigDecimal("3.0"), 
            "/"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("3.333333"), result.getResult());
    }

    @Test
    void testDivisionByZero() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10.0"), 
            new BigDecimal("0"), 
            "/"
        );

        // When & Then
        assertThrows(ArithmeticException.class, () -> {
            calculatorService.calculate(request, testSessionId);
        });
    }

    @Test
    void testInvalidOperation() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10.0"),
            new BigDecimal("5.0"),
            "%"
        );

        // Mock validationService to throw specific exception
        doThrow(new IllegalArgumentException(CalculatorConstants.ERROR_INVALID_OPERATION))
            .when(validationService).validateCalculatorRequest(request);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(request, testSessionId);
        });

        assertEquals(CalculatorConstants.ERROR_INVALID_OPERATION, exception.getMessage());
    }

    // ==================== Undo/Redo Operations ====================

    @Test
    void testUndoOperation() {

        TwoNumberCalculatorRequest request1 = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );
        TwoNumberCalculatorRequest request2 = new TwoNumberCalculatorRequest(
            new BigDecimal("15"), new BigDecimal("3"), "*"
        );

        calculatorService.calculate(request1, testSessionId);
        calculatorService.calculate(request2, testSessionId);

        // When - Undo last operation
        CalculationResult undoResult = calculatorService.undo(testSessionId);

        // Then - Should return to previous state
        assertEquals(new BigDecimal("15"), undoResult.getResult());
        assertEquals(1, undoResult.getStackSizes().getUndoCount());
        assertEquals(1, undoResult.getStackSizes().getRedoCount());
    }

    @Test
    void testRedoOperation() {

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "*"
        );
        calculatorService.calculate(request, testSessionId);
        calculatorService.undo(testSessionId);

        CalculationResult redoResult = calculatorService.redo(testSessionId);

        assertEquals(new BigDecimal("50"), redoResult.getResult());
        assertEquals(1, redoResult.getStackSizes().getUndoCount());
        assertEquals(0, redoResult.getStackSizes().getRedoCount());
    }

    @Test
    void testUndoWithNoHistory() {
        assertThrows(IllegalStateException.class, () -> {
            calculatorService.undo(testSessionId);
        });
    }

    @Test
    void testRedoWithNoHistory() {
        assertThrows(IllegalStateException.class, () -> {
            calculatorService.redo(testSessionId);
        });
    }

    @Test
    void testUndoRedoChain() {

        TwoNumberCalculatorRequest request1 = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );
        TwoNumberCalculatorRequest request2 = new TwoNumberCalculatorRequest(
            new BigDecimal("15"), new BigDecimal("2"), "*"
        );
        TwoNumberCalculatorRequest request3 = new TwoNumberCalculatorRequest(
            new BigDecimal("30"), new BigDecimal("3"), "/"
        );

        calculatorService.calculate(request1, testSessionId);
        calculatorService.calculate(request2, testSessionId);
        calculatorService.calculate(request3, testSessionId);

        CalculationResult undo1 = calculatorService.undo(testSessionId);
        CalculationResult undo2 = calculatorService.undo(testSessionId);
        CalculationResult redo1 = calculatorService.redo(testSessionId);

        assertEquals(new BigDecimal("30"), undo1.getResult());
        assertEquals(new BigDecimal("15"), undo2.getResult());
        assertEquals(new BigDecimal("30"), redo1.getResult());
    }

    @Test
    void testMaxHistoryLimit() {

        for (int i = 1; i <= 5; i++) {
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal(i), new BigDecimal("1"), "+"
            );
            calculatorService.calculate(request, testSessionId);
        }

        CalculationResult result = calculatorService.calculate(
            new TwoNumberCalculatorRequest(new BigDecimal("6"), new BigDecimal("1"), "+"),
            testSessionId
        );
        // 6 operations but we just keep 3 steps undo
        assertEquals(3, result.getStackSizes().getUndoCount());
    }

    @Test
    void testRedoStackClearedOnNewOperation() {
        // Given - Perform operation, undo, then new operation
        TwoNumberCalculatorRequest request1 = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );
        TwoNumberCalculatorRequest request2 = new TwoNumberCalculatorRequest(
            new BigDecimal("20"), new BigDecimal("10"), "*"
        );

        calculatorService.calculate(request1, testSessionId);
        calculatorService.undo(testSessionId);
        calculatorService.calculate(request2, testSessionId);

        // When - Try to redo
        // Then - Should throw exception because redo stack was cleared
        assertThrows(IllegalStateException.class, () -> {
            calculatorService.redo(testSessionId);
        });
    }

    // ==================== Session Management ====================

    @Test
    void testClearSession() {
        // Given - Perform some operations
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );
        calculatorService.calculate(request, testSessionId);

        // When
        StackSizesResponse clearResult = calculatorService.clear(testSessionId);

        // Then
        assertEquals(0, clearResult.getUndoCount());
        assertEquals(0, clearResult.getRedoCount());

        // Verify undo/redo are no longer available
        assertThrows(IllegalStateException.class, () -> {
            calculatorService.undo(testSessionId);
        });
    }

    @Test
    void testMultipleSessions() {
        // Given
        String session1 = calculatorService.initSession();
        String session2 = calculatorService.initSession();

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );

        // When
        CalculationResult result1 = calculatorService.calculate(request, session1);
        CalculationResult result2 = calculatorService.calculate(request, session2);

        // Then - Sessions should be independent
        assertEquals(new BigDecimal("15.000000"), result1.getResult());
        assertEquals(new BigDecimal("15.000000"), result2.getResult());
        assertEquals(1, result1.getStackSizes().getUndoCount());
        assertEquals(1, result2.getStackSizes().getUndoCount());
    }

    // ==================== Boundary Value Tests ====================

    @Test
    void testZeroOperations() {
        // Given
        TwoNumberCalculatorRequest addRequest = new TwoNumberCalculatorRequest(
            new BigDecimal("0"), new BigDecimal("0"), "+"
        );
        TwoNumberCalculatorRequest subRequest = new TwoNumberCalculatorRequest(
            new BigDecimal("0"), new BigDecimal("0"), "-"
        );
        TwoNumberCalculatorRequest mulRequest = new TwoNumberCalculatorRequest(
            new BigDecimal("0"), new BigDecimal("5"), "*"
        );

        // When
        CalculationResult addResult = calculatorService.calculate(addRequest, testSessionId);
        CalculationResult subResult = calculatorService.calculate(subRequest, testSessionId);
        CalculationResult mulResult = calculatorService.calculate(mulRequest, testSessionId);

        // Then
        assertEquals(new BigDecimal("0.000000"), addResult.getResult());
        assertEquals(new BigDecimal("0.000000"), subResult.getResult());
        assertEquals(new BigDecimal("0.000000"), mulResult.getResult());
    }

    @Test
    void testNegativeNumbers() {

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("-10.5"), new BigDecimal("-3.2"), "+"
        );
        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("-13.700000"), result.getResult());
    }

    // ==================== Validation Service Integration ====================

    @Test
    void testValidationServiceCalled() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );

        calculatorService.calculate(request, testSessionId);

        verify(validationService, times(1)).validateCalculatorRequest(request);
        verify(validationService, times(1)).validateSessionId(testSessionId);
    }

    @Test
    void testValidationServiceThrowsException() {
        // Given
        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );

        doThrow(new IllegalArgumentException("Invalid request"))
            .when(validationService).validateCalculatorRequest(any());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            calculatorService.calculate(request, testSessionId);
        });
    }

    // ==================== Different Sessions) ====================

    @Test
    void testConcurrentSessions() {
        // Given
        String[] sessions = new String[10];
        for (int i = 0; i < 10; i++) {
            sessions[i] = calculatorService.initSession();
        }

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
            new BigDecimal("10"), new BigDecimal("5"), "+"
        );

        // When - Perform operations on multiple sessions concurrently
        for (String session : sessions) {
            CalculationResult result = calculatorService.calculate(request, session);
            assertEquals(new BigDecimal("15.000000"), result.getResult());
        }

        // Then - All sessions should work independently
        for (String session : sessions) {
            CalculationResult undoResult = calculatorService.undo(session);
            assertEquals(new BigDecimal("10"), undoResult.getResult());
        }
    }

    // ==================== Precision Tests ====================


    @Test
    void testPrecisionToSixDecimalPlacesRoundDown() {

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("1.11111111"),
                new BigDecimal("2.11111111"),
                "+"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("3.222222"), result.getResult());
    }
    @Test
    void testPrecisionToSixDecimalPlacesRoundUp() {

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("1.3333333"),
                new BigDecimal("2.3333333"),
                "+"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("3.666667"), result.getResult());
    }

    @Test
    void testLargeNumbers() {

        TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("999999999.999999"),
                new BigDecimal("0.000001"),
                "+"
        );

        CalculationResult result = calculatorService.calculate(request, testSessionId);

        assertEquals(new BigDecimal("1000000000.000000"), result.getResult());
    }
} 