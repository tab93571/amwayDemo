package com.example.demoproject.luckydraw.dto;

/**
 * Error response DTO
 */
public class ErrorResponse {
    private final int errorCode;
    private final String message;
    
    public ErrorResponse(int errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }
    
    public int getErrorCode() {
        return errorCode;
    }
    
    public String getMessage() {
        return message;
    }
} 