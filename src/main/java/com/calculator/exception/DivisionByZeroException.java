package com.calculator.exception;

public class DivisionByZeroException extends CalculatorException {

    private static final String ERROR_CODE = "DIVISION_BY_ZERO";

    public DivisionByZeroException() {
        super(ERROR_CODE, "Division por cero no esta permitida.");
    }
}
