package com.calculator.controller;

import com.calculator.dto.OperationRequest;
import com.calculator.dto.OperationResponse;
import com.calculator.dto.UnaryOperationRequest;
import com.calculator.exception.DivisionByZeroException;
import com.calculator.exception.NegativeSqrtException;
import com.calculator.service.CalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Slice tests para {@link CalculatorController}.
 *
 * - @WebMvcTest carga solo la capa web; CalculatorService se inyecta como doble
 *   de prueba (mock) mediante @MockitoBean, aislando el controlador completamente.
 * - Patrón AAA estricto en cada método.
 * - Se verifica: código HTTP, cuerpo JSON, invocación del servicio y manejo de
 *   excepciones de negocio propagadas al GlobalExceptionHandler.
 * - Cobertura: 100 % de ramas (éxito + error de validación + excepción de dominio)
 *   en cada endpoint.
 */
@WebMvcTest(CalculatorController.class)
@DisplayName("CalculatorController Web Layer Tests")
class CalculatorControllerTest {

    private static final String BASE_URL = "/api/calculator";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Doble de prueba: intercepta llamadas al servicio
    @MockitoBean
    private CalculatorService calculatorService;

    // =========================================================================
    // POST /add
    // =========================================================================

    @Nested
    @DisplayName("POST /add")
    class AddEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna resultado correcto")
        void add_validRequest_returns200WithResult() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(7.0, 3.0);
            when(calculatorService.add(7.0, 3.0)).thenReturn(10.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("add"))
                    .andExpect(jsonPath("$.result").value(10.0));

            verify(calculatorService).add(7.0, 3.0);
        }

        @Test
        @DisplayName("400 Bad Request operando A es nulo")
        void add_nullOperandA_returns400() throws Exception {
            // Arrange
            String body = """
                    { "operandB": 3.0 }
                    """;

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("400 Bad Request operando B es nulo")
        void add_nullOperandB_returns400() throws Exception {
            // Arrange
            String body = """
                    { "operandA": 5.0 }
                    """;

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
        }

        @Test
        @DisplayName("400 Bad Request – body completamente vacío")
        void add_emptyBody_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/add")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // =========================================================================
    // POST /subtract
    // =========================================================================

    @Nested
    @DisplayName("POST /subtract")
    class SubtractEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna diferencia correcta")
        void subtract_validRequest_returns200WithResult() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(10.0, 4.0);
            when(calculatorService.subtract(10.0, 4.0)).thenReturn(6.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/subtract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("subtract"))
                    .andExpect(jsonPath("$.result").value(6.0));

            verify(calculatorService).subtract(10.0, 4.0);
        }

        @Test
        @DisplayName("400 Bad Request operandos nulos")
        void subtract_nullOperands_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/subtract")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
        }
    }

    // =========================================================================
    // POST /multiply
    // =========================================================================

    @Nested
    @DisplayName("POST /multiply")
    class MultiplyEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna producto correcto")
        void multiply_validRequest_returns200WithResult() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(3.0, 4.0);
            when(calculatorService.multiply(3.0, 4.0)).thenReturn(12.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/multiply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("multiply"))
                    .andExpect(jsonPath("$.result").value(12.0));

            verify(calculatorService).multiply(3.0, 4.0);
        }

        @Test
        @DisplayName("400 Bad Request operandos nulos")
        void multiply_nullOperands_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/multiply")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // =========================================================================
    // POST /divide
    // =========================================================================

    @Nested
    @DisplayName("POST /divide")
    class DivideEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna cociente correcto")
        void divide_validRequest_returns200WithResult() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(10.0, 2.0);
            when(calculatorService.divide(10.0, 2.0)).thenReturn(5.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/divide")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("divide"))
                    .andExpect(jsonPath("$.result").value(5.0));

            verify(calculatorService).divide(10.0, 2.0);
        }

        @Test
        @DisplayName("422 Unprocessable Entity servicio lanza DivisionByZeroException")
        void divide_serviceThrowsDivisionByZero_returns422() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(8.0, 0.0);
            when(calculatorService.divide(8.0, 0.0))
                    .thenThrow(new DivisionByZeroException());

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/divide")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.errorCode").value("DIVISION_BY_ZERO"))
                    .andExpect(jsonPath("$.message").value("Division por cero no esta permitida."))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @Test
        @DisplayName("400 Bad Request operandos nulos")
        void divide_nullOperands_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/divide")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }

    // =========================================================================
    // POST /sqrt
    // =========================================================================

    @Nested
    @DisplayName("POST /sqrt")
    class SqrtEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna raíz correcta")
        void sqrt_validRequest_returns200WithResult() throws Exception {
            // Arrange
            UnaryOperationRequest request = new UnaryOperationRequest(25.0);
            when(calculatorService.sqrt(25.0)).thenReturn(5.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/sqrt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("sqrt"))
                    .andExpect(jsonPath("$.result").value(5.0));

            verify(calculatorService).sqrt(25.0);
        }

        @Test
        @DisplayName("422 Unprocessable Entity servicio lanza NegativeSqrtException")
        void sqrt_serviceThrowsNegativeSqrt_returns422() throws Exception {
            // Arrange
            UnaryOperationRequest request = new UnaryOperationRequest(-16.0);
            when(calculatorService.sqrt(-16.0))
                    .thenThrow(new NegativeSqrtException(-16.0));

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/sqrt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.errorCode").value("NEGATIVE_SQRT"))
                    .andExpect(jsonPath("$.message").value(
                            org.hamcrest.Matchers.containsString("-16.0")))
                    .andExpect(jsonPath("$.timestamp").isNotEmpty());
        }

        @Test
        @DisplayName("400 Bad Request operand es nulo")
        void sqrt_nullOperand_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/sqrt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errorCode").value("VALIDATION_ERROR"));
        }
    }

    // =========================================================================
    // POST /power
    // =========================================================================

    @Nested
    @DisplayName("POST /power")
    class PowerEndpoint {

        @Test
        @DisplayName("200 OK delega al servicio y retorna potencia correcta")
        void power_validRequest_returns200WithResult() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(2.0, 8.0);
            when(calculatorService.power(2.0, 8.0)).thenReturn(256.0);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/power")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.operation").value("power"))
                    .andExpect(jsonPath("$.result").value(256.0));

            verify(calculatorService).power(2.0, 8.0);
        }

        @Test
        @DisplayName("200 OK exponente negativo retorna fracción")
        void power_negativeExponent_returns200WithFraction() throws Exception {
            // Arrange
            OperationRequest request = new OperationRequest(2.0, -1.0);
            when(calculatorService.power(2.0, -1.0)).thenReturn(0.5);

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/power")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.result").value(0.5));
        }

        @Test
        @DisplayName("400 Bad Request operandos nulos")
        void power_nullOperands_returns400() throws Exception {
            // Arrange
            String body = "{}";

            // Act & Assert
            mockMvc.perform(post(BASE_URL + "/power")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isBadRequest());
        }
    }
}