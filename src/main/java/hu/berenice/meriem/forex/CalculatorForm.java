package hu.berenice.meriem.forex;

/** Form-backing object for the calculator page (needs getters/setters for th:field). */
public class CalculatorForm {

    private Double number1;
    private Double number2;
    private String operation = "add";

    public Double getNumber1() {
        return number1;
    }

    public void setNumber1(Double number1) {
        this.number1 = number1;
    }

    public Double getNumber2() {
        return number2;
    }

    public void setNumber2(Double number2) {
        this.number2 = number2;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
