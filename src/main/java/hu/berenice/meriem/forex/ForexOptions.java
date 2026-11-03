package hu.berenice.meriem.forex;

import java.util.List;

/** Drop-down values shared by all Forex pages (yours and your partner's). */
public final class ForexOptions {

    private ForexOptions() {
    }

    public record Option(String value, String label) {
    }

    public static final List<String> INSTRUMENTS = List.of(
            "EUR_USD", "GBP_USD", "USD_JPY", "USD_CHF", "AUD_USD",
            "USD_CAD", "NZD_USD", "EUR_GBP", "EUR_JPY", "GBP_JPY");

    // Note: "M1" is one minute, plain "M" is one month.
    public static final List<Option> GRANULARITIES = List.of(
            new Option("M1", "M1 – 1 minute"),
            new Option("M5", "M5 – 5 minutes"),
            new Option("M15", "M15 – 15 minutes"),
            new Option("M30", "M30 – 30 minutes"),
            new Option("H1", "H1 – 1 hour"),
            new Option("H4", "H4 – 4 hours"),
            new Option("D", "D – daily"),
            new Option("W", "W – weekly"),
            new Option("M", "M – monthly"));

    /** The task asks for the last 10 historical prices. */
    public static final int HISTORY_COUNT = 10;

    public static boolean isValidGranularity(String value) {
        return GRANULARITIES.stream().anyMatch(o -> o.value().equals(value));
    }
}
