package com.calculator.service;

import com.calculator.exception.DivisionByZeroException;
import com.calculator.exception.NegativeSqrtException;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    
    public double add(double a, double b) {
        return a + b;
    }
    
    public double subtract(double a, double b) {
        return a - b;
    }
    
    public double multiply(double a, double b) {
        return a * b;
    }
    
    public double divide(double a, double b) {
        if (b == 0) {
            throw new DivisionByZeroException();
        }
        return a / b;
    }
    
    public double sqrt(double a) {
        if (a < 0) {
            throw new NegativeSqrtException(a);
        }
        return Math.sqrt(a);
    }
    
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }
}
