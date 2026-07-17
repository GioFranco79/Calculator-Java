package com.calculator.controller;

import com.calculator.dto.OperationRequest;
import com.calculator.dto.OperationResponse;
import com.calculator.dto.UnaryOperationRequest;
import com.calculator.service.CalculatorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calculator")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }
    
    @PostMapping("/add")
    public ResponseEntity<OperationResponse> add(@Valid @RequestBody OperationRequest request) {
        double result = calculatorService.add(request.operandA(), request.operandB());
        return ResponseEntity.ok(new OperationResponse("add", result));
    }

    @PostMapping("/subtract")
    public ResponseEntity<OperationResponse> subtract(@Valid @RequestBody OperationRequest request) {
        double result = calculatorService.subtract(request.operandA(), request.operandB());
        return ResponseEntity.ok(new OperationResponse("subtract", result));
    }

    @PostMapping("/multiply")
    public ResponseEntity<OperationResponse> multiply(@Valid @RequestBody OperationRequest request) {
        double result = calculatorService.multiply(request.operandA(), request.operandB());
        return ResponseEntity.ok(new OperationResponse("multiply", result));
    }

    @PostMapping("/divide")
    public ResponseEntity<OperationResponse> divide(@Valid @RequestBody OperationRequest request) {
        double result = calculatorService.divide(request.operandA(), request.operandB());
        return ResponseEntity.ok(new OperationResponse("divide", result));
    }

    @PostMapping("/power")
    public ResponseEntity<OperationResponse> power(@Valid @RequestBody OperationRequest request) {
        double result = calculatorService.power(request.operandA(), request.operandB());
        return ResponseEntity.ok(new OperationResponse("power", result));
    }   

    @PostMapping("/sqrt")
    public ResponseEntity<OperationResponse> sqrt(@Valid @RequestBody UnaryOperationRequest request) {
        double result = calculatorService.sqrt(request.operand());
        return ResponseEntity.ok(new OperationResponse("sqrt", result));
    }
}
