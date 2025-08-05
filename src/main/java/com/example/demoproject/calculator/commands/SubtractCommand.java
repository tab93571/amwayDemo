package com.example.demoproject.calculator.commands;

import com.example.demoproject.calculator.CalculatorCommand;
import com.example.demoproject.calculator.util.PrecisionUtil;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Command for subtraction operation
 */
public class SubtractCommand implements CalculatorCommand {

    private BigDecimal left, right;

    public SubtractCommand(BigDecimal left, BigDecimal right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public BigDecimal execute() {
        return left.subtract(right, PrecisionUtil.getMathContext());
    }

    @Override
    public BigDecimal undo() {
        return left;
    }
}