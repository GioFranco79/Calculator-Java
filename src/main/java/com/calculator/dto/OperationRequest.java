package com.calculator.dto;

import jakarta.validation.constraints.NotNull;

public record OperationRequest(

        @NotNull(message = "operando A no debe ser nulo")
        Double operandA,

        @NotNull(message = "operando B no debe ser nulo")
        Double operandB
) {}
