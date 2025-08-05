package com.example.demoproject.calculator.constants;

/**
 * Constants for calculator validation and error messages
 */
public class CalculatorConstants {
    
    // Error Messages
    public static final String ERROR_DIVISION_BY_ZERO = "Division by zero is not allowed";
    public static final String ERROR_INVALID_OPERATION = "Invalid operation. Supported operations: +, -, *, /";
    public static final String ERROR_NULL_OR_EMPTY_SESSION = "Session ID cannot be null or empty";
    public static final String ERROR_NUMBER_TOO_LARGE = "Number is too large for calculation";
    public static final String ERROR_NUMBER_TOO_SMALL = "Number is too small for calculation";
    
    // Validation Rules
    public static final double MAX_NUMBER_VALUE = 1e15; // 10^15
    public static final double MIN_NUMBER_VALUE = -1e15; // -10^15
    
    // Supported Operations
    public static final String[] SUPPORTED_OPERATIONS = {"+", "-", "*", "/"};
    
    // Precision and Rounding Settings
    public static final int DEFAULT_PRECISION = 6; // 6 decimal places
    public static final java.math.RoundingMode ROUNDING_MODE = java.math.RoundingMode.HALF_EVEN;
    
    private CalculatorConstants() {
        // Utility class - prevent instantiation
    }
} 