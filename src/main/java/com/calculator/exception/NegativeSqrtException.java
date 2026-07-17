package com.calculator.exception;

public class NegativeSqrtException extends CalculatorException {

    private static final String ERROR_CODE = "NEGATIVE_SQRT";

    public NegativeSqrtException(double value) {
        super(ERROR_CODE,
                "La Raiz cuadrada de un número negativo no esta definida en los números reales. Valor: " + value);
    }
}
