package com.calculator.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Exception Hierarchy Unit Tests")
class CalculatorExceptionTest {

    // =========================================================================
    // CalculatorException (clase base)
    // =========================================================================

    @Nested
    @DisplayName("CalculatorException")
    class CalculatorExceptionTests {

        @Test
        @DisplayName("constructor asigna errorCode y message correctamente")
        void constructor_setsErrorCodeAndMessage() {
            // Arrange
            String code = "TEST_CODE";
            String msg  = "test message";

            // Act
            CalculatorException ex = new CalculatorException(code, msg);

            // Assert
            assertThat(ex.getErrorCode()).isEqualTo(code);
            assertThat(ex.getMessage()).isEqualTo(msg);
        }

        @Test
        @DisplayName("es subclase de RuntimeException")
        void calculatorException_isRuntimeException() {
            // Arrange & Act
            CalculatorException ex = new CalculatorException("C", "m");

            // Assert
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }
    }

    // =========================================================================
    // DivisionByZeroException
    // =========================================================================

    @Nested
    @DisplayName("DivisionByZeroException")
    class DivisionByZeroExceptionTests {

        @Test
        @DisplayName("errorCode es DIVISION_BY_ZERO")
        void constructor_setsCorrectErrorCode() {
            // Arrange & Act
            DivisionByZeroException ex = new DivisionByZeroException();

            // Assert
            assertThat(ex.getErrorCode()).isEqualTo("DIVISION_BY_ZERO");
        }

        @Test
        @DisplayName("mensaje contiene texto descriptivo")
        void constructor_setsDescriptiveMessage() {
            // Arrange & Act
            DivisionByZeroException ex = new DivisionByZeroException();

            // Assert
            assertThat(ex.getMessage()).isEqualTo("Division por cero no esta permitida.");
        }

        @Test
        @DisplayName("es subclase de CalculatorException")
        void divisionByZeroException_isCalculatorException() {
            // Arrange & Act
            DivisionByZeroException ex = new DivisionByZeroException();

            // Assert
            assertThat(ex).isInstanceOf(CalculatorException.class);
        }
    }

    // =========================================================================
    // NegativeSqrtException
    // =========================================================================

    @Nested
    @DisplayName("NegativeSqrtException")
    class NegativeSqrtExceptionTests {

        @Test
        @DisplayName("errorCode es NEGATIVE_SQRT")
        void constructor_setsCorrectErrorCode() {
            // Arrange & Act
            NegativeSqrtException ex = new NegativeSqrtException(-4.0);

            // Assert
            assertThat(ex.getErrorCode()).isEqualTo("NEGATIVE_SQRT");
        }

        @Test
        @DisplayName("mensaje incluye el valor negativo proporcionado")
        void constructor_embedsValueInMessage() {
            // Arrange
            double value = -16.0;

            // Act
            NegativeSqrtException ex = new NegativeSqrtException(value);

            // Assert
            assertThat(ex.getMessage()).contains(String.valueOf(value));
        }

        @Test
        @DisplayName("mensaje contiene texto descriptivo del dominio")
        void constructor_containsDomainDescription() {
            // Arrange & Act
            NegativeSqrtException ex = new NegativeSqrtException(-1.0);

            // Assert
            assertThat(ex.getMessage())
                    .contains("La Raiz cuadrada de un número negativo no esta definida en los números reales");
        }

        @Test
        @DisplayName("es subclase de CalculatorException")
        void negativeSqrtException_isCalculatorException() {
            // Arrange & Act
            NegativeSqrtException ex = new NegativeSqrtException(-1.0);

            // Assert
            assertThat(ex).isInstanceOf(CalculatorException.class);
        }
    }
}
