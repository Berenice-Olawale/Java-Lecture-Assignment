package hu.berenice.meriem.forex;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/** Plain JUnit 5 tests - no Spring involved, so they run in milliseconds. */
class CalculatorServiceTest {

    private final CalculatorService calculator = new CalculatorService();

    @Test
    void addsTwoNumbers() {
        assertEquals(5.0, calculator.add(2, 3));
    }

    @Test
    void addsNegativeNumbers() {
        assertEquals(-5.0, calculator.add(-2, -3));
    }

    @Test
    void subtractsTwoNumbers() {
        assertEquals(-1.0, calculator.subtract(2, 3));
    }

    @Test
    void multipliesTwoNumbers() {
        assertEquals(6.0, calculator.multiply(2, 3));
    }

    @Test
    void multiplyByZeroGivesZero() {
        assertEquals(0.0, calculator.multiply(12345, 0));
    }

    @Test
    void dividesTwoNumbers() {
        assertEquals(2.5, calculator.divide(5, 2));
    }

    @Test
    void divisionByZeroThrows() {
        ArithmeticException ex = assertThrows(ArithmeticException.class, () -> calculator.divide(1, 0));
        assertEquals("Division by zero is not allowed", ex.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "add,      2, 3,  5",
            "subtract, 2, 3, -1",
            "multiply, 2, 3,  6",
            "divide,   9, 3,  3"
    })
    void calculateChoosesTheRightOperation(String operation, double a, double b, double expected) {
        assertEquals(expected, calculator.calculate(a, b, operation));
    }

    @Test
    void unknownOperationThrows() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> calculator.calculate(1, 1, "power"));
        assertEquals("Unknown operation: power", ex.getMessage());
    }
}
