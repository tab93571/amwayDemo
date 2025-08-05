package com.example.demoproject.calculator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;


public class TwoNumberCalculatorRequest {

    private BigDecimal left = new BigDecimal("0");
    
    @NotNull(message = "right number is required")
    private BigDecimal right;

    @NotBlank(message = "Operation is required")
    private String operation; // "+", "-", "*", "/"

    public TwoNumberCalculatorRequest() {}

    public TwoNumberCalculatorRequest(BigDecimal left, BigDecimal right, String operation) {
        this.left = left;
        this.right = right;
        this.operation = operation;
    }

    public BigDecimal getLeft() {
        return left;
    }

    public void setLeft(BigDecimal left) {
        this.left = left;
    }

    public BigDecimal getRight() {
        return right;
    }

    public void setRight(BigDecimal right) {
        this.right = right;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
} 