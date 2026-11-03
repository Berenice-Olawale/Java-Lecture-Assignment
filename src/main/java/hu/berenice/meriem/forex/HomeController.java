package hu.berenice.meriem.forex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    /** One card on the main page. */
    public record MenuItem(String title, String description, String url) {
    }

    private static final List<MenuItem> MENU = List.of(
            new MenuItem("SOAP – MNB rates", "Official exchange rates of the Hungarian National Bank, with a chart.", "/soap"),
            new MenuItem("Account Overview", "View your Forex trading account information.", "/forex/account"),
            new MenuItem("Actual Prices", "Check the current bid and ask price of a currency pair.", "/forex/actual-price"),
            new MenuItem("Historical Prices", "Explore the last candles for any instrument and granularity.", "/forex/historical-price"),
            new MenuItem("Open a Position", "Go long or short at the market price.", "/forex/open"),
            new MenuItem("Open Positions", "See every position currently open on the account.", "/forex/positions"),
            new MenuItem("Close a Position", "Close a trade by its trade ID at the market price.", "/forex/close"),
            new MenuItem("Test: Calculator", "A small form application covered by JUnit and MockMvc tests.", "/test")
    );

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("menuItems", MENU);
        return "index";
    }
}
