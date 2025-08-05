package com.example.demoproject.calculator;

import java.math.BigDecimal;

/**
 * Command interface for calculator operations
 * Part of the Command Pattern implementation
 */
public interface CalculatorCommand {

    BigDecimal execute();
    BigDecimal undo();
} 