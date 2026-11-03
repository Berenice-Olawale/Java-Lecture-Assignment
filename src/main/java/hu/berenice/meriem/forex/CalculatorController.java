package hu.berenice.meriem.forex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/** Test menu: a small form application (GET shows the form, POST calculates). */
@Controller
public class CalculatorController {

    private final CalculatorService calculator;

    public CalculatorController(CalculatorService calculator) {
        this.calculator = calculator;
    }

    @GetMapping("/test")
    public String showForm(Model model) {
        model.addAttribute("form", new CalculatorForm());
        return "calculator";
    }

    @PostMapping("/test")
    public String calculate(@ModelAttribute("form") CalculatorForm form,
                            BindingResult bindingResult,
                            Model model) {
        if (bindingResult.hasErrors() || form.getNumber1() == null || form.getNumber2() == null) {
            model.addAttribute("error", "Please enter two valid numbers.");
            return "calculator";
        }
        try {
            double result = calculator.calculate(form.getNumber1(), form.getNumber2(), form.getOperation());
            model.addAttribute("result", result);
        } catch (ArithmeticException | IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "calculator";
    }
}
