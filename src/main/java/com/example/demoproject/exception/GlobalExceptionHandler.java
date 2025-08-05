package com.example.demoproject.exception;

import com.example.demoproject.luckydraw.dto.ErrorResponse;
import com.example.demoproject.luckydraw.constants.ErrorConstants;
import com.example.demoproject.luckydraw.exception.LuckyDrawException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * Global exception handler for the entire application
 * Handles exceptions from all packages: admin, auth, calculator, luckydraw
 */
@RestControllerAdvice(basePackages = "com.example.demoproject")
public class GlobalExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
        logger.info("GlobalExceptionHandler initialized for all packages!");
    }

    /**
     * Handle LuckyDrawException
     */
    @ExceptionHandler(LuckyDrawException.class)
    public ResponseEntity<ErrorResponse> handleLuckyDrawException(LuckyDrawException e) {
        logger.info("Handling LuckyDrawException: {} - {}", e.getErrorCode(), e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handle IllegalArgumentException (from calculator, admin, auth packages)
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.info("Handling IllegalArgumentException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorConstants.ErrorType.INVALID_REQUEST.getCode(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle IllegalStateException (from calculator package)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException e) {
        logger.info("Handling IllegalStateException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorConstants.ErrorType.INVALID_STATE.getCode(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Handle ArithmeticException (from calculator package - division by zero)
     */
    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ErrorResponse> handleArithmeticException(ArithmeticException e) {
        logger.info("Handling ArithmeticException: {}", e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorConstants.ErrorType.INVALID_OPERATION.getCode(),
            e.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Handle bean‐validation errors (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // collect just the default messages from each failed @NotNull/@Max/@Min, etc.
        String combined = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        logger.debug("Validation failed: {}", combined);
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorConstants.ErrorType.INVALID_REQUEST.getCode(),
                ErrorConstants.ErrorType.INVALID_REQUEST.getMessage() + combined
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        logger.error("Handling general exception: {}", e.getMessage(), e);
        ErrorResponse errorResponse = new ErrorResponse(
            ErrorConstants.ErrorType.INTERNAL_SERVER_ERROR.getCode(), 
            ErrorConstants.ErrorType.INTERNAL_SERVER_ERROR.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
} 