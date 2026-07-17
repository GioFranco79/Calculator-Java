package com.calculator.exception;

public class CalculatorException extends RuntimeException {

    private final String errorCode;

    public CalculatorException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
