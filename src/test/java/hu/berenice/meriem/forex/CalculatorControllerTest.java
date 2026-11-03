package hu.berenice.meriem.forex;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * MockMvc sends fake HTTP requests to the controller without starting a real server.
 * (Spring Boot 4: @AutoConfigureMockMvc lives in org.springframework.boot.webmvc.test.autoconfigure.)
 */
@SpringBootTest
@AutoConfigureMockMvc
class CalculatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getShowsTheEmptyForm() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attributeDoesNotExist("result"));
    }

    @Test
    void postAddsTwoNumbers() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "2").param("number2", "3").param("operation", "add"))
                .andExpect(status().isOk())
                .andExpect(view().name("calculator"))
                .andExpect(model().attribute("result", 5.0))
                .andExpect(content().string(containsString("5.0")));
    }

    @Test
    void postMultipliesTwoNumbers() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "4").param("number2", "2.5").param("operation", "multiply"))
                .andExpect(model().attribute("result", 10.0));
    }

    @Test
    void postDividesTwoNumbers() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "9").param("number2", "2").param("operation", "divide"))
                .andExpect(model().attribute("result", 4.5));
    }

    @Test
    void divisionByZeroShowsAnErrorInsteadOfAResult() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "1").param("number2", "0").param("operation", "divide"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Division by zero is not allowed"))
                .andExpect(model().attributeDoesNotExist("result"));
    }

    @Test
    void missingNumberShowsAnError() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "1").param("operation", "add"))
                .andExpect(model().attribute("error", "Please enter two valid numbers."))
                .andExpect(model().attributeDoesNotExist("result"));
    }

    @Test
    void textInsteadOfANumberShowsAnError() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "abc").param("number2", "2").param("operation", "add"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("error", "Please enter two valid numbers."));
    }

    @Test
    void unknownOperationShowsAnError() throws Exception {
        mockMvc.perform(post("/test")
                        .param("number1", "1").param("number2", "2").param("operation", "power"))
                .andExpect(model().attribute("error", "Unknown operation: power"));
    }
}
