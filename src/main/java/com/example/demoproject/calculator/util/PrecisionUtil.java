package com.example.demoproject.calculator.util;

import com.example.demoproject.calculator.constants.CalculatorConstants;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Utility class for handling precision and rounding in calculations
 */
@Component
public class PrecisionUtil {
    
    private static final MathContext MATH_CONTEXT = new MathContext(
        34, // Use high precision for calculations, then round to 6 decimal places
        CalculatorConstants.ROUNDING_MODE
    );
   
    public static BigDecimal applyPrecision(BigDecimal value) {
        if (value == null) {
            return null;
        }
        
        // Round to 6 decimal places using HALF_EVEN (banker's rounding)
        return value.setScale(CalculatorConstants.DEFAULT_PRECISION, CalculatorConstants.ROUNDING_MODE);
    }
    
    /**
     * Get MathContext for calculations
     * @return MathContext with specified precision and rounding mode
     */
    public static MathContext getMathContext() {
        return MATH_CONTEXT;
    }
} 