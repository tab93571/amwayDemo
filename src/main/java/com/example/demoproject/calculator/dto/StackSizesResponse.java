package com.example.demoproject.calculator.dto;

/**
 * Response DTO for undo/redo stack sizes
 */
public class StackSizesResponse {
    private final int undoCount;
    private final int redoCount;
    
    public StackSizesResponse(int undoCount, int redoCount) {
        this.undoCount = undoCount;
        this.redoCount = redoCount;
    }
    
    public int getUndoCount() {
        return undoCount;
    }
    
    public int getRedoCount() {
        return redoCount;
    }
} 