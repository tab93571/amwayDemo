package com.example.demoproject.calculator.validation;

import com.example.demoproject.calculator.TwoNumberCalculatorRequest;
import com.example.demoproject.calculator.constants.CalculatorConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for CalculatorValidationService
 */
@DisplayName("CalculatorValidationService Tests")
class CalculatorValidationServiceTest {

    private CalculatorValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new CalculatorValidationService();
    }

    @Nested
    @DisplayName("Session ID Validation Tests")
    class SessionIdValidationTests {

        @Test
        @DisplayName("Should pass validation for valid session ID")
        void shouldPassValidationForValidSessionId() {
           
            String validSessionId = "user123-session456";

            
            assertDoesNotThrow(() -> validationService.validateSessionId(validSessionId));
        }

        @Test
        @DisplayName("Should throw exception for null session ID")
        void shouldThrowExceptionForNullSessionId() {

            String nullSessionId = null;

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateSessionId(nullSessionId)
            );
            assertEquals(CalculatorConstants.ERROR_NULL_OR_EMPTY_SESSION, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty session ID")
        void shouldThrowExceptionForEmptySessionId() {

            String emptySessionId = "";

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateSessionId(emptySessionId)
            );
            assertEquals(CalculatorConstants.ERROR_NULL_OR_EMPTY_SESSION, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for whitespace-only session ID")
        void shouldThrowExceptionForWhitespaceOnlySessionId() {

            String whitespaceSessionId = "   ";

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateSessionId(whitespaceSessionId)
            );
            assertEquals(CalculatorConstants.ERROR_NULL_OR_EMPTY_SESSION, exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Calculator Request Validation Tests")
    class CalculatorRequestValidationTests {

        @Test
        @DisplayName("Should throw exception for division by zero")
        void shouldThrowExceptionForDivisionByZero() {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("100"),
                BigDecimal.ZERO,
                "/"
            );

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_DIVISION_BY_ZERO, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid operation")
        void shouldThrowExceptionForInvalidOperation() {

            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("10"),
                new BigDecimal("5"),
                "%"
            );

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_INVALID_OPERATION, exception.getMessage());
        }

        @ParameterizedTest
        @ValueSource(strings = {"+", "-", "*", "/"})
        @DisplayName("Should pass validation for all supported operations")
        void shouldPassValidationForAllSupportedOperations(String operation) {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("10"),
                new BigDecimal("5"),
                operation
            );

            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }
    }

    @Nested
    @DisplayName("Number Size Validation Tests")
    class NumberSizeValidationTests {

        @Test
        @DisplayName("Should pass validation for numbers within valid range")
        void shouldPassValidationForNumbersWithinValidRange() {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("1000000"),
                new BigDecimal("-500000"),
                "+"
            );

            
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }

        @Test
        @DisplayName("Should pass validation for maximum allowed positive number")
        void shouldPassValidationForMaximumAllowedPositiveNumber() {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal(String.valueOf(CalculatorConstants.MAX_NUMBER_VALUE)),
                new BigDecimal("1"),
                "+"
            );

            
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }

        @Test
        @DisplayName("Should pass validation for minimum allowed negative number")
        void shouldPassValidationForMinimumAllowedNegativeNumber() {
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal(String.valueOf(CalculatorConstants.MIN_NUMBER_VALUE)),
                new BigDecimal("1"),
                "+"
            );
            
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }

        @Test
        @DisplayName("Should throw exception for number too large (left operand)")
        void shouldThrowExceptionForNumberTooLargeLeftOperand() {
            
            BigDecimal tooLargeNumber = new BigDecimal(String.valueOf(CalculatorConstants.MAX_NUMBER_VALUE + 1));
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                tooLargeNumber,
                new BigDecimal("1"),
                "+"
            );

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_NUMBER_TOO_LARGE, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for number too large (right operand)")
        void shouldThrowExceptionForNumberTooLargeRightOperand() {
           
            BigDecimal tooLargeNumber = new BigDecimal(String.valueOf(CalculatorConstants.MAX_NUMBER_VALUE + 1));
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("1"),
                tooLargeNumber,
                "+"
            );

            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_NUMBER_TOO_LARGE, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for number too small (left operand)")
        void shouldThrowExceptionForNumberTooSmallLeftOperand() {
            BigDecimal tooSmallNumber = new BigDecimal(String.valueOf(CalculatorConstants.MIN_NUMBER_VALUE - 1));
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                tooSmallNumber,
                new BigDecimal("1"),
                "+"
            );
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_NUMBER_TOO_SMALL, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for number too small (right operand)")
        void shouldThrowExceptionForNumberTooSmallRightOperand() {
           
            BigDecimal tooSmallNumber = new BigDecimal(String.valueOf(CalculatorConstants.MIN_NUMBER_VALUE - 1));
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("1"),
                tooSmallNumber,
                "+"
            );
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_NUMBER_TOO_SMALL, exception.getMessage());
        }

        @Test
        @DisplayName("Should pass validation for zero values")
        void shouldPassValidationForZeroValues() {
            
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                "+"
            );
            
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }

        @Test
        @DisplayName("Should pass validation for decimal numbers")
        void shouldPassValidationForDecimalNumbers() {
            
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("3.14159"),
                new BigDecimal("2.71828"),
                "*"
            );
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }

        @Test
        @DisplayName("Should pass validation for very large decimal numbers within range")
        void shouldPassValidationForVeryLargeDecimalNumbersWithinRange() {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("999999999999999.999999"),
                new BigDecimal("-999999999999999.999999"),
                "+"
            );
            assertDoesNotThrow(() -> validationService.validateCalculatorRequest(request));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCasesAndBoundaryTests {

        @Test
        @DisplayName("Should handle operation with leading/trailing whitespace")
        void shouldHandleOperationWithWhitespace() {
           
            TwoNumberCalculatorRequest request = new TwoNumberCalculatorRequest(
                new BigDecimal("10"),
                new BigDecimal("5"),
                " + "  // Note: This fails as invalid operation since " + " is not in supported operations
            );
            
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> validationService.validateCalculatorRequest(request)
            );
            assertEquals(CalculatorConstants.ERROR_INVALID_OPERATION, exception.getMessage());
        }
    }
} 