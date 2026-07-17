package com.calculator.exception;

import com.calculator.dto.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler – Unit Tests")
class GlobalExceptionHandlerTest {

    // -------------------------------------------------------------------------
    // SUT (instancia directa, sin Spring – prueba pura de la lógica del handler)
    // -------------------------------------------------------------------------

    private GlobalExceptionHandler sut;

    @BeforeEach
    void setUp() {
        // Arrange (shared)
        sut = new GlobalExceptionHandler();
    }

    // =========================================================================
    // handleDivisionByZero
    // =========================================================================

    @Nested
    @DisplayName("handleDivisionByZero()")
    class HandleDivisionByZero {

        @Test
        @DisplayName("retorna 422 con errorCode DIVISION_BY_ZERO")
        void handleDivisionByZero_returns422WithCorrectErrorCode() {
            // Arrange
            DivisionByZeroException ex = new DivisionByZeroException();

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleDivisionByZero(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("DIVISION_BY_ZERO");
            assertThat(response.getBody().message()).isEqualTo("Division por cero no esta permitida.");
            assertThat(response.getBody().timestamp()).isNotNull();
        }
    }

    // =========================================================================
    // handleNegativeSqrt
    // =========================================================================

    @Nested
    @DisplayName("handleNegativeSqrt()")
    class HandleNegativeSqrt {

        @Test
        @DisplayName("retorna 422 con errorCode NEGATIVE_SQRT")
        void handleNegativeSqrt_returns422WithCorrectErrorCode() {
            // Arrange
            NegativeSqrtException ex = new NegativeSqrtException(-9.0);

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleNegativeSqrt(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("NEGATIVE_SQRT");
            assertThat(response.getBody().message()).contains("-9.0");
            assertThat(response.getBody().timestamp()).isNotNull();
        }
    }

    // =========================================================================
    // handleCalculatorException (CalculatorException genérica, NO subclases)
    // =========================================================================

    @Nested
    @DisplayName("handleCalculatorException()")
    class HandleCalculatorException {

        @Test
        @DisplayName("retorna 400 para CalculatorException genérica")
        void handleCalculatorException_returns400() {
            // Arrange – subclase anónima para representar un error genérico de dominio
            CalculatorException ex = new CalculatorException("GENERIC_ERROR", "algo salió mal");

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleCalculatorException(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("GENERIC_ERROR");
            assertThat(response.getBody().message()).isEqualTo("algo salió mal");
        }
    }

    // =========================================================================
    // handleValidation (MethodArgumentNotValidException)
    // =========================================================================

    @Nested
    @DisplayName("handleValidation()")
    class HandleValidation {

        @Test
        @DisplayName("retorna 400 con detalle de los campos inválidos")
        void handleValidation_returns400WithFieldErrors() {
            // Arrange – mock de MethodArgumentNotValidException y su BindingResult
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);

            FieldError fieldError = new FieldError("operationRequest", "operandB",
                    "operandB must not be null");

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleValidation(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(response.getBody().message()).contains("operandB");
            assertThat(response.getBody().message()).contains("operandB must not be null");
        }

        @Test
        @DisplayName("concatena múltiples errores de campo con coma")
        void handleValidation_multipleFieldErrors_concatenatesMessages() {
            // Arrange
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);

            FieldError errorA = new FieldError("req", "operandA", "operandA must not be null");
            FieldError errorB = new FieldError("req", "operandB", "operandB must not be null");

            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(errorA, errorB));

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleValidation(ex);

            // Assert
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().message()).contains("operandA").contains("operandB");
        }
    }

    // =========================================================================
    // handleConstraintViolation
    // =========================================================================

    @Nested
    @DisplayName("handleConstraintViolation()")
    class HandleConstraintViolation {

        @Test
        @DisplayName("retorna 400 con el mensaje de la excepción")
        void handleConstraintViolation_returns400() {
            // Arrange – ConstraintViolationException requiere un Set; usamos vacío
            ConstraintViolationException ex = new ConstraintViolationException(
                    "violación de restricción", Set.of());

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleConstraintViolation(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("VALIDATION_ERROR");
            assertThat(response.getBody().message()).isEqualTo("violación de restricción");
        }
    }

    // =========================================================================
    // handleGeneric (fallback 500)
    // =========================================================================

    @Nested
    @DisplayName("handleGeneric()")
    class HandleGeneric {

        @Test
        @DisplayName("retorna 500 con errorCode INTERNAL_ERROR")
        void handleGeneric_returns500() {
            // Arrange
            Exception ex = new RuntimeException("fallo inesperado");

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleGeneric(ex);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().errorCode()).isEqualTo("INTERNAL_ERROR");
            assertThat(response.getBody().message()).isEqualTo("Un error inesperado ocurrio.");
        }

        @Test
        @DisplayName("timestamp no es nulo en respuesta de error genérico")
        void handleGeneric_timestampIsPresent() {
            // Arrange
            Exception ex = new Exception("error");

            // Act
            ResponseEntity<ErrorResponse> response = sut.handleGeneric(ex);

            // Assert
            assertThat(response.getBody()).isNotNull();
            assertThat(response.getBody().timestamp()).isNotNull();
        }
    }
}
