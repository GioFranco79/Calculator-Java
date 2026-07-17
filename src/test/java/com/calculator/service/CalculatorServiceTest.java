package com.calculator.service;

import com.calculator.exception.DivisionByZeroException;
import com.calculator.exception.NegativeSqrtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CalculatorService – Unit Tests")
class CalculatorServiceTest {

    // -------------------------------------------------------------------------
    // SUT
    // -------------------------------------------------------------------------

    private CalculatorService sut;

    @BeforeEach
    void setUp() {
        // Arrange (shared) – instancia directa para inyección por constructor implícita
        sut = new CalculatorService();
    }

    // =========================================================================
    // ADD
    // =========================================================================

    @Nested
    @DisplayName("add()")
    class Add {

        @Test
        @DisplayName("suma dos positivos")
        void add_twoPositives_returnsSum() {
            // Arrange
            double a = 5.0, b = 3.0;

            // Act
            double result = sut.add(a, b);

            // Assert
            assertThat(result).isEqualTo(8.0);
        }

        @Test
        @DisplayName("suma dos negativos")
        void add_twoNegatives_returnsSum() {
            // Arrange
            double a = -4.0, b = -6.0;

            // Act
            double result = sut.add(a, b);

            // Assert
            assertThat(result).isEqualTo(-10.0);
        }

        @Test
        @DisplayName("suma positivo con negativo")
        void add_positiveAndNegative_returnsCorrectSum() {
            // Arrange
            double a = 10.0, b = -3.0;

            // Act
            double result = sut.add(a, b);

            // Assert
            assertThat(result).isEqualTo(7.0);
        }

        @Test
        @DisplayName("suma con cero devuelve el mismo número")
        void add_withZero_returnsOperand() {
            // Arrange
            double a = 42.0, b = 0.0;

            // Act
            double result = sut.add(a, b);

            // Assert
            assertThat(result).isEqualTo(42.0);
        }

        @Test
        @DisplayName("suma de decimales con precisión")
        void add_decimals_returnsCorrectPrecision() {
            // Arrange
            double a = 0.1, b = 0.2;

            // Act
            double result = sut.add(a, b);

            // Assert – tolerancia estándar IEEE-754
            assertThat(result).isCloseTo(0.3, within(1e-9));
        }
    }

    // =========================================================================
    // SUBTRACT
    // =========================================================================

    @Nested
    @DisplayName("subtract()")
    class Subtract {

        @Test
        @DisplayName("resta dos positivos")
        void subtract_twoPositives_returnsDifference() {
            // Arrange
            double a = 10.0, b = 4.0;

            // Act
            double result = sut.subtract(a, b);

            // Assert
            assertThat(result).isEqualTo(6.0);
        }

        @Test
        @DisplayName("resta que produce resultado negativo")
        void subtract_smallerFromLarger_returnsNegative() {
            // Arrange
            double a = 3.0, b = 8.0;

            // Act
            double result = sut.subtract(a, b);

            // Assert
            assertThat(result).isEqualTo(-5.0);
        }

        @Test
        @DisplayName("resta dos negativos")
        void subtract_twoNegatives_returnsCorrectDifference() {
            // Arrange
            double a = -2.0, b = -5.0;

            // Act
            double result = sut.subtract(a, b);

            // Assert
            assertThat(result).isEqualTo(3.0);
        }

        @Test
        @DisplayName("restar cero no cambia el valor")
        void subtract_zero_returnsSameValue() {
            // Arrange
            double a = 99.0, b = 0.0;

            // Act
            double result = sut.subtract(a, b);

            // Assert
            assertThat(result).isEqualTo(99.0);
        }

        @Test
        @DisplayName("número menos sí mismo es cero")
        void subtract_sameNumbers_returnsZero() {
            // Arrange
            double a = 7.5, b = 7.5;

            // Act
            double result = sut.subtract(a, b);

            // Assert
            assertThat(result).isEqualTo(0.0);
        }
    }

    // =========================================================================
    // MULTIPLY
    // =========================================================================

    @Nested
    @DisplayName("multiply()")
    class Multiply {

        @Test
        @DisplayName("multiplica dos positivos")
        void multiply_twoPositives_returnsProduct() {
            // Arrange
            double a = 3.0, b = 4.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(12.0);
        }

        @Test
        @DisplayName("multiplicar por cero devuelve cero")
        void multiply_byZero_returnsZero() {
            // Arrange
            double a = 99999.0, b = 0.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("multiplicar por uno devuelve el mismo número")
        void multiply_byOne_returnsOperand() {
            // Arrange
            double a = 42.0, b = 1.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(42.0);
        }

        @Test
        @DisplayName("dos negativos producen un positivo")
        void multiply_twoNegatives_returnsPositive() {
            // Arrange
            double a = -3.0, b = -5.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(15.0);
        }

        @Test
        @DisplayName("positivo por negativo produce negativo")
        void multiply_positiveByNegative_returnsNegative() {
            // Arrange
            double a = 6.0, b = -7.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(-42.0);
        }

        @Test
        @DisplayName("multiplica decimales")
        void multiply_decimals_returnsCorrectProduct() {
            // Arrange
            double a = 2.5, b = 4.0;

            // Act
            double result = sut.multiply(a, b);

            // Assert
            assertThat(result).isEqualTo(10.0);
        }
    }

    // =========================================================================
    // DIVIDE
    // =========================================================================

    @Nested
    @DisplayName("divide()")
    class Divide {

        // --- Branch: b != 0 (happy path) ---

        @Test
        @DisplayName("divide dos positivos")
        void divide_twoPositives_returnsQuotient() {
            // Arrange
            double a = 10.0, b = 2.0;

            // Act
            double result = sut.divide(a, b);

            // Assert
            assertThat(result).isEqualTo(5.0);
        }

        @Test
        @DisplayName("divide que produce decimal")
        void divide_producesDecimalResult() {
            // Arrange
            double a = 1.0, b = 3.0;

            // Act
            double result = sut.divide(a, b);

            // Assert
            assertThat(result).isCloseTo(0.3333, within(1e-4));
        }

        @Test
        @DisplayName("divide negativo entre positivo")
        void divide_negativeByPositive_returnsNegative() {
            // Arrange
            double a = -9.0, b = 3.0;

            // Act
            double result = sut.divide(a, b);

            // Assert
            assertThat(result).isEqualTo(-3.0);
        }

        @Test
        @DisplayName("divide cero entre número → resultado cero")
        void divide_zeroNumerator_returnsZero() {
            // Arrange
            double a = 0.0, b = 5.0;

            // Act
            double result = sut.divide(a, b);

            // Assert
            assertThat(result).isEqualTo(0.0);
        }

        // --- Branch: b == 0 (guard clause) ---

        @Test
        @DisplayName("BRANCH: divisor = 0 → lanza DivisionByZeroException")
        void divide_byZero_throwsDivisionByZeroException() {
            // Arrange
            double a = 5.0, b = 0.0;

            // Act & Assert
            DivisionByZeroException ex = assertThrows(
                    DivisionByZeroException.class,
                    () -> sut.divide(a, b)
            );
            assertThat(ex.getErrorCode()).isEqualTo("DIVISION_BY_ZERO");
            assertThat(ex.getMessage()).contains("Division por cero");
        }

        @Test
        @DisplayName("BRANCH: divisor = -0.0 (IEEE-754) → lanza DivisionByZeroException")
        void divide_byNegativeZero_throwsDivisionByZeroException() {
            // Arrange – -0.0 == 0.0 en Java (IEEE-754), mismo branch
            double a = 1.0, b = -0.0;

            // Act & Assert
            assertThrows(DivisionByZeroException.class, () -> sut.divide(a, b));
        }
    }

    // =========================================================================
    // SQRT
    // =========================================================================

    @Nested
    @DisplayName("sqrt()")
    class Sqrt {

        // --- Branch: a >= 0 (happy path) ---

        @Test
        @DisplayName("raíz cuadrada de un cuadrado perfecto")
        void sqrt_perfectSquare_returnsExactRoot() {
            // Arrange
            double a = 25.0;

            // Act
            double result = sut.sqrt(a);

            // Assert
            assertThat(result).isEqualTo(5.0);
        }

        @Test
        @DisplayName("raíz cuadrada de cero es cero")
        void sqrt_zero_returnsZero() {
            // Arrange
            double a = 0.0;

            // Act
            double result = sut.sqrt(a);

            // Assert
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("raíz cuadrada de número no cuadrado perfecto (precisión)")
        void sqrt_imperfectSquare_returnsApproximateRoot() {
            // Arrange
            double a = 2.0;

            // Act
            double result = sut.sqrt(a);

            // Assert
            assertThat(result).isCloseTo(1.41421356, within(1e-7));
        }

        @Test
        @DisplayName("raíz cuadrada de un decimal positivo")
        void sqrt_positiveDecimal_returnsRoot() {
            // Arrange
            double a = 0.25;

            // Act
            double result = sut.sqrt(a);

            // Assert
            assertThat(result).isEqualTo(0.5);
        }

        // --- Branch: a < 0 (guard clause) ---

        @Test
        @DisplayName("BRANCH: radicando negativo → lanza NegativeSqrtException")
        void sqrt_negativeValue_throwsNegativeSqrtException() {
            // Arrange
            double a = -9.0;

            // Act & Assert
            NegativeSqrtException ex = assertThrows(
                    NegativeSqrtException.class,
                    () -> sut.sqrt(a)
            );
            assertThat(ex.getErrorCode()).isEqualTo("NEGATIVE_SQRT");
            assertThat(ex.getMessage()).contains("-9.0");
        }

        @Test
        @DisplayName("BRANCH: radicando = -0.0001 (límite del dominio negativo)")
        void sqrt_smallNegative_throwsNegativeSqrtException() {
            // Arrange
            double a = -0.0001;

            // Act & Assert
            assertThrows(NegativeSqrtException.class, () -> sut.sqrt(a));
        }

        @Test
        @DisplayName("BRANCH: Double.NEGATIVE_INFINITY → lanza NegativeSqrtException")
        void sqrt_negativeInfinity_throwsNegativeSqrtException() {
            // Arrange
            double a = Double.NEGATIVE_INFINITY;

            // Act & Assert
            assertThrows(NegativeSqrtException.class, () -> sut.sqrt(a));
        }
    }

    // =========================================================================
    // POWER
    // =========================================================================

    @Nested
    @DisplayName("power()")
    class Power {

        @Test
        @DisplayName("base positiva con exponente entero positivo")
        void power_positiveBasePositiveExponent_returnsCorrectResult() {
            // Arrange
            double base = 2.0, exponent = 10.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(1024.0);
        }

        @Test
        @DisplayName("exponente cero devuelve 1 (cualquier base)")
        void power_zeroExponent_returnsOne() {
            // Arrange
            double base = 999.0, exponent = 0.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(1.0);
        }

        @Test
        @DisplayName("exponente uno devuelve la base")
        void power_exponentOne_returnsBase() {
            // Arrange
            double base = 7.0, exponent = 1.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(7.0);
        }

        @Test
        @DisplayName("exponente negativo devuelve fracción")
        void power_negativeExponent_returnsFraction() {
            // Arrange
            double base = 2.0, exponent = -1.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(0.5);
        }

        @Test
        @DisplayName("base cero con exponente positivo devuelve cero")
        void power_zeroBase_returnsZero() {
            // Arrange
            double base = 0.0, exponent = 5.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("exponente fraccionario equivale a raíz")
        void power_fractionalExponent_returnsRoot() {
            // Arrange  – 9^0.5 = √9 = 3
            double base = 9.0, exponent = 0.5;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isCloseTo(3.0, within(1e-9));
        }

        @Test
        @DisplayName("base negativa con exponente par devuelve positivo")
        void power_negativeBaseEvenExponent_returnsPositive() {
            // Arrange
            double base = -3.0, exponent = 2.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(9.0);
        }

        @Test
        @DisplayName("base negativa con exponente impar devuelve negativo")
        void power_negativeBaseOddExponent_returnsNegative() {
            // Arrange
            double base = -2.0, exponent = 3.0;

            // Act
            double result = sut.power(base, exponent);

            // Assert
            assertThat(result).isEqualTo(-8.0);
        }
    }
}
