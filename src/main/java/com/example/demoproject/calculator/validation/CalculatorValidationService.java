package com.example.demoproject.calculator.validation;

import com.example.demoproject.calculator.TwoNumberCalculatorRequest;
import com.example.demoproject.calculator.constants.CalculatorConstants;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Service for validating calculator inputs
 */
@Service
public class CalculatorValidationService {
    
    /**
     * Validate calculator request
     * @param request The calculator request
     * @throws IllegalArgumentException if validation fails
     */
    public void validateCalculatorRequest(TwoNumberCalculatorRequest request) {
        validateNumbers(request.getLeft(), request.getRight(), request.getOperation());
        validateOperation(request.getOperation());
    }

    /**
     * Validate session ID
     * @param sessionId The session ID to validate
     * @throws IllegalArgumentException if validation fails
     */
    public void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException(CalculatorConstants.ERROR_NULL_OR_EMPTY_SESSION);
        }
    }
    
    /**
     * Validate numbers for calculation
     * @param left Left number
     * @param right Right number
     * @param operation The operation being performed
     * @throws IllegalArgumentException if validation fails
     */
    private void validateNumbers(BigDecimal left, BigDecimal right, String operation) {

        if ("/".equals(operation) && right.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException(CalculatorConstants.ERROR_DIVISION_BY_ZERO);
        }

        validateNumberSize(left);
        validateNumberSize(right);
    }
    
    /**
     * Validate operation
     * @param operation The operation to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateOperation(String operation) {
        
        if (!Arrays.asList(CalculatorConstants.SUPPORTED_OPERATIONS).contains(operation)) {
            throw new IllegalArgumentException(CalculatorConstants.ERROR_INVALID_OPERATION);
        }
    }
    
    /**
     * Validate number size
     * @param number The number to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateNumberSize(BigDecimal number) {
        BigDecimal maxValue = BigDecimal.valueOf(CalculatorConstants.MAX_NUMBER_VALUE);
        BigDecimal minValue = BigDecimal.valueOf(CalculatorConstants.MIN_NUMBER_VALUE);
        
        if (number.compareTo(maxValue) > 0) {
            throw new IllegalArgumentException(CalculatorConstants.ERROR_NUMBER_TOO_LARGE);
        }
        
        if (number.compareTo(minValue) < 0) {
            throw new IllegalArgumentException(CalculatorConstants.ERROR_NUMBER_TOO_SMALL);
        }
    }
} 