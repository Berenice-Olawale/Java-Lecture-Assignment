package hu.berenice.meriem.forex;

import org.springframework.stereotype.Service;

/** The four-operation calculator used by the Test menu. */
@Service
public class CalculatorService {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed");
        }
        return a / b;
    }

    public double calculate(double a, double b, String operation) {
        if (operation == null) {
            throw new IllegalArgumentException("Unknown operation: null");
        }
        return switch (operation) {
            case "add" -> add(a, b);
            case "subtract" -> subtract(a, b);
            case "multiply" -> multiply(a, b);
            case "divide" -> divide(a, b);
            default -> throw new IllegalArgumentException("Unknown operation: " + operation);
        };
    }
}
