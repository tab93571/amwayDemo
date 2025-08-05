package com.example.demoproject.luckydraw.constants;

/**
 * Error constants for lucky draw system
 */
public class ErrorConstants {
    
    /**
     * Error enum that binds error codes with messages
     */
    public enum ErrorType {
        USER_DRAW_LIMIT_REACHED(1001, "You have reached your draw limit for this activity"),
        USER_MULTIPLE_DRAW_LIMIT_REACHED(1002, "You cannot perform this many draws. You have reached your limit for this activity"),
        ACTIVITY_DRAW_LIMIT_REACHED(1003, "This activity has reached its total draw limit"),
        ACTIVITY_NOT_FOUND(1004, "Activity not found"),
        NO_PRIZES_AVAILABLE(1005, "No prizes available for this activity"),
        UNAUTHORIZED_ACCESS(1007, "Unauthorized access"),
        INTERNAL_SERVER_ERROR(1008, "An internal server error occurred"),
        INVALID_REQUEST(1009, "Invalid request "),
        INVALID_STATE(1010, "Invalid state"),
        INVALID_OPERATION(1011, "Invalid operation"),

        SYSTEM_BUSY(1012, "db is busy");
        
        private final int code;
        private final String message;
        
        ErrorType(int code, String message) {
            this.code = code;
            this.message = message;
        }
        
        public int getCode() {
            return code;
        }
        
        public String getMessage() {
            return message;
        }
    }
} 