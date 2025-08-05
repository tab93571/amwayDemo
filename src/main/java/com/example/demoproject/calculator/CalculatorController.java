package com.example.demoproject.calculator;

import com.example.demoproject.calculator.dto.StackSizesResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;


/**
 * REST Controller for calculator operations
 */
@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")
public class CalculatorController {

    @Autowired
    private CalculatorService calculatorService;

    /**
     * Perform calculation with two numbers
     * @param request Two number calculation request
     * @return Calculation result
     */
    @PostMapping("/calculate")
    public ResponseEntity<CalculatorService.CalculationResult> calculate(@RequestBody TwoNumberCalculatorRequest request, 
                                                                      @RequestHeader(value = "X-Session-ID") String sessionId) {
        CalculatorService.CalculationResult result = calculatorService.calculate(request, sessionId);
        return ResponseEntity.ok(result);
    }

    /**
     * Undo last calculation
     * @param sessionId Session identifier
     * @return Previous calculation result
     */
    @PostMapping("/{sessionId}/undo")
    public ResponseEntity<CalculatorService.CalculationResult> undo(@PathVariable String sessionId) {
        CalculatorService.CalculationResult result = calculatorService.undo(sessionId);
        return ResponseEntity.ok(result);
    }

    /**
     * Redo last undone calculation
     * @param sessionId Session identifier
     * @return Redone calculation result
     */
    @PostMapping("/{sessionId}/redo")
    public ResponseEntity<CalculatorService.CalculationResult> redo(@PathVariable String sessionId) {
        CalculatorService.CalculationResult result = calculatorService.redo(sessionId);
        return ResponseEntity.ok(result);
    }

    /**
     * Initialize a new calculator session
     * @return Session initialization response
     */
    @PostMapping("/init-session")
    public ResponseEntity<Map<String, String>> initSession() {

        String sessionId = calculatorService.initSession();
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        return ResponseEntity.ok(response);
    }

    /**
     * Clear calculation history for a session
     * @param sessionId Session identifier
     * @return Stack sizes after clear (should be 0,0)
     */
    @PostMapping("/{sessionId}/clear")
    public ResponseEntity<StackSizesResponse> clear(@PathVariable String sessionId) {

        StackSizesResponse response = calculatorService.clear(sessionId);
        return ResponseEntity.ok(response);
    }
} 