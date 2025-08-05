package com.example.demoproject.luckydraw.exception;

import com.example.demoproject.luckydraw.constants.ErrorConstants;

/**
 * Custom exception for lucky draw system
 */
public class LuckyDrawException extends RuntimeException {
    
    private final int errorCode;
    
    public LuckyDrawException(ErrorConstants.ErrorType errorType) {
        super(errorType.getMessage());
        this.errorCode = errorType.getCode();
    }
    
    public int getErrorCode() {
        return errorCode;
    }
} 