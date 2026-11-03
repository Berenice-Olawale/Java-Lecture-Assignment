package hu.berenice.meriem.forex;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;

/** Forex-account, Forex-ActPrice and Forex-HistPrice menus. */
@Controller
@RequestMapping("/forex")
public class ForexInfoController {

    private final OandaMarketService market;

    public ForexInfoController(OandaMarketService market) {
        this.market = market;
    }

    @GetMapping("/account")
    public String account(Model model) {
        try {
            model.addAttribute("account", market.getAccountSummary());
        } catch (RestClientException | IllegalStateException e) {
            model.addAttribute("error", "Could not read the account from OANDA: " + e.getMessage());
        }
        return "forex/account";
    }

    @GetMapping("/actual-price")
    public String actualPrice(@RequestParam(name = "instrument", required = false) String instrument,
                              Model model) {
        model.addAttribute("instruments", ForexOptions.INSTRUMENTS);
        model.addAttribute("selectedInstrument", instrument);

        if (instrument != null) {
            if (!ForexOptions.INSTRUMENTS.contains(instrument)) {
                model.addAttribute("error", "Unknown instrument: " + instrument);
            } else {
                try {
                    model.addAttribute("quote", market.getPrice(instrument));
                } catch (RestClientException | IllegalStateException e) {
                    model.addAttribute("error", "Could not read the price from OANDA: " + e.getMessage());
                }
            }
        }
        return "forex/actual-price";
    }

    @GetMapping("/historical-price")
    public String historicalPrice(@RequestParam(name = "instrument", required = false) String instrument,
                                  @RequestParam(name = "granularity", required = false) String granularity,
                                  Model model) {
        model.addAttribute("instruments", ForexOptions.INSTRUMENTS);
        model.addAttribute("granularities", ForexOptions.GRANULARITIES);
        model.addAttribute("selectedInstrument", instrument);
        model.addAttribute("selectedGranularity", granularity);

        if (instrument != null && granularity != null) {
            if (!ForexOptions.INSTRUMENTS.contains(instrument) || !ForexOptions.isValidGranularity(granularity)) {
                model.addAttribute("error", "Unknown instrument or granularity.");
            } else {
                try {
                    model.addAttribute("candles",
                            market.getCandles(instrument, granularity, ForexOptions.HISTORY_COUNT));
                } catch (RestClientException | IllegalStateException e) {
                    model.addAttribute("error", "Could not read the prices from OANDA: " + e.getMessage());
                }
            }
        }
        return "forex/historical-price";
    }
}
