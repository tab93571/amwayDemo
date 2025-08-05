package com.example.demoproject.calculator;

import com.example.demoproject.calculator.commands.AddCommand;
import com.example.demoproject.calculator.commands.DivideCommand;
import com.example.demoproject.calculator.commands.MultiplyCommand;
import com.example.demoproject.calculator.commands.SubtractCommand;
import com.example.demoproject.calculator.dto.StackSizesResponse;
import com.example.demoproject.calculator.validation.CalculatorValidationService;
import com.example.demoproject.calculator.util.PrecisionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalculatorService {

    @Autowired
    private CalculatorValidationService validationService;

    private final Map<String, Deque<CalculatorCommand>> undoStacks = new ConcurrentHashMap<>();
    private final Map<String, Deque<CalculatorCommand>> redoStacks = new ConcurrentHashMap<>();
    private static final int MAX_HISTORY = 3;

    /**
     * Perform calculation with stateful behavior
     * @param request Two number calculation request
     * @param sessionId Session identifier
     * @return Calculation result with session ID
     */
    public CalculationResult calculate(TwoNumberCalculatorRequest request, String sessionId) {

        validationService.validateCalculatorRequest(request);
        validationService.validateSessionId(sessionId);

        BigDecimal left = request.getLeft();
        BigDecimal right = request.getRight();
        String operation = request.getOperation();

        if(undoStacks.get(sessionId) == null){
            undoStacks.put(sessionId, new LinkedList<>());
        }

        if(redoStacks.get(sessionId) == null){
            redoStacks.put(sessionId, new LinkedList<>());
        }

        CalculatorCommand cmd;
        switch (operation) {
            case "+":
                cmd = new AddCommand(left, right);
                break;
            case "-":
                cmd = new SubtractCommand(left, right);
                break;
            case "*":
                cmd = new MultiplyCommand(left, right);
                break;
            case "/":
                cmd = new DivideCommand(left, right);
                break;
            default:
                throw new IllegalArgumentException("Unsupported operation: " + operation);
        }

        BigDecimal result = cmd.execute();

        result = PrecisionUtil.applyPrecision(result);

        Deque<CalculatorCommand> undoStack = undoStacks.get(sessionId);
        undoStack.push(cmd);
        if (undoStack.size() > MAX_HISTORY) {
            undoStack.removeLast();
        }
        redoStacks.get(sessionId).clear();

        StackSizesResponse stackSizes = getStackSizes(sessionId);
        
        return new CalculationResult(result, stackSizes);
    }

    /**
     * Undo last calculation
     * @param sessionId Session identifier
     * @return Previous calculation result
     */
    public CalculationResult undo(String sessionId) {
        
        Deque<CalculatorCommand> undoStack = undoStacks.get(sessionId);
        Deque<CalculatorCommand> redoStack = redoStacks.get(sessionId);
        
        if (undoStack == null || undoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to undo");
        }

        CalculatorCommand cmd = undoStack.pop();
        BigDecimal restored = cmd.undo();

        redoStack.push(cmd);
        
        // Get current stack sizes
        StackSizesResponse stackSizes = getStackSizes(sessionId);
        
        return new CalculationResult(restored, stackSizes);
    }

    /**
     * Redo last undone calculation
     * @param sessionId Session identifier
     * @return Redone calculation result
     */
    public CalculationResult redo(String sessionId) {
        
        Deque<CalculatorCommand> redoStack = redoStacks.get(sessionId);
        Deque<CalculatorCommand> undoStack = undoStacks.get(sessionId);
        
        if (redoStack == null || redoStack.isEmpty()) {
            throw new IllegalStateException("Nothing to redo");
        }

        CalculatorCommand cmd = redoStack.pop();
        BigDecimal result = cmd.execute();

        undoStack.push(cmd);
        if (undoStack.size() > MAX_HISTORY) {
            undoStack.removeFirst();
        }
        
        // Get current stack sizes
        StackSizesResponse stackSizes = getStackSizes(sessionId);
        
        return new CalculationResult(result, stackSizes);
    }


    /**
     * Get undo/redo counts for a session
     * @param sessionId Session identifier
     * @return Stack sizes response
     */
    private StackSizesResponse getStackSizes(String sessionId) {
        
        Deque<CalculatorCommand> undoStack = undoStacks.get(sessionId);
        Deque<CalculatorCommand> redoStack = redoStacks.get(sessionId);
        
        int undoCount = undoStack != null ? undoStack.size() : 0;
        int redoCount = redoStack != null ? redoStack.size() : 0;
        
        return new StackSizesResponse(undoCount, redoCount);
    }

    /**
     * Initialize a new calculator session
     * @return Generated session ID
     */
    public String initSession() {
        String sessionId = "calc_" + UUID.randomUUID().toString().substring(0, 8);
        undoStacks.put(sessionId, new LinkedList<>());
        redoStacks.put(sessionId, new LinkedList<>());
        return sessionId;
    }

    /**
     * Clear calculation history for a session
     * @param sessionId Session identifier
     * @return Stack sizes after clear (should be 0,0)
     */
    public StackSizesResponse clear(String sessionId) {
        
        undoStacks.remove(sessionId);
        redoStacks.remove(sessionId);
        
        // Return stack sizes after clear
        return new StackSizesResponse(0, 0);
    }

    public static class CalculationResult {
        private final BigDecimal result;
        private final StackSizesResponse stackSizes;

        public CalculationResult(BigDecimal result, StackSizesResponse stackSizes) {
            this.result = result;
            this.stackSizes = stackSizes;
        }

        public BigDecimal getResult() {
            return result;
        }

        public StackSizesResponse getStackSizes() {
            return stackSizes;
        }
    }
} 