package hu.berenice.meriem.forex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/forex")
public class ForexTradeController {

    static final int MAX_UNITS = 100;

    private final OandaTradingService trading;

    public ForexTradeController(OandaTradingService trading) {
        this.trading = trading;
    }


    @GetMapping("/open")
    public String openForm(Model model) {
        model.addAttribute("instruments", ForexOptions.INSTRUMENTS);
        model.addAttribute("maxUnits", MAX_UNITS);
        return "forex/open";
    }

    @PostMapping("/open")
    public String open(@RequestParam("instrument") String instrument,
                       @RequestParam("units") int units,
                       RedirectAttributes redirect) {
        if (!ForexOptions.INSTRUMENTS.contains(instrument)) {
            redirect.addFlashAttribute("error", "Unknown instrument: " + instrument);
        } else if (units == 0 || Math.abs(units) > MAX_UNITS) {
            redirect.addFlashAttribute("error",
                    "Units must be between -" + MAX_UNITS + " and " + MAX_UNITS + ", and not 0.");
        } else {
            try {
                redirect.addFlashAttribute("opened", trading.openPosition(instrument, units));
            } catch (RestClientException | IllegalStateException e) {
                redirect.addFlashAttribute("error", "Could not open the position: " + e.getMessage());
            }
        }
        return "redirect:/forex/open";
    }


    @GetMapping("/positions")
    public String positions(Model model) {
        try {
            model.addAttribute("trades", trading.listOpenTrades());
        } catch (RestClientException | IllegalStateException e) {
            model.addAttribute("error", "Could not read the open positions: " + e.getMessage());
        }
        return "forex/positions";
    }

    @GetMapping("/close")
    public String closeForm() {
        return "forex/close";
    }

    @PostMapping("/close")
    public String close(@RequestParam("tradeId") long tradeId, RedirectAttributes redirect) {
        if (tradeId <= 0) {
            redirect.addFlashAttribute("error", "The trade ID must be a positive number.");
        } else {
            try {
                redirect.addFlashAttribute("closed", trading.closeTrade(tradeId));
            } catch (RestClientException | IllegalStateException e) {
                redirect.addFlashAttribute("error", "Could not close the trade: " + e.getMessage());
            }
        }
        return "redirect:/forex/close";
    }
}