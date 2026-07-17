package com.calculator.dto;

import jakarta.validation.constraints.NotNull;

public record UnaryOperationRequest(

        @NotNull(message = "operando no debe ser nulo")
        Double operand
) {}
