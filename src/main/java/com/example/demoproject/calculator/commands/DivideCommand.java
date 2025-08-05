package com.example.demoproject.calculator.commands;

import com.example.demoproject.calculator.CalculatorCommand;
import com.example.demoproject.calculator.util.PrecisionUtil;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Command for division operation
 */
public class DivideCommand implements CalculatorCommand {
    private BigDecimal left, right;

    public DivideCommand(BigDecimal left, BigDecimal right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public BigDecimal execute() {
        return left.divide(right, PrecisionUtil.getMathContext());
    }

    @Override
    public BigDecimal undo() {
        return left;
    }
}