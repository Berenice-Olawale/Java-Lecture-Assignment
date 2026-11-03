package hu.berenice.meriem.forex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.List;

/** Read-only OANDA calls: account summary, current price, historical candles. */
@Service
public class OandaMarketService {

    /** What the "actual price" page shows. */
    public record PriceQuote(String instrument, String time, String bid, String ask,
                             String spread, boolean tradeable) {
    }

    /** One row of the "historical price" table. */
    public record CandleRow(String time, String open, String high, String low,
                            String close, long volume, boolean complete) {
    }

    private final RestClient client;
    private final String accountId;

    public OandaMarketService(RestClient oandaRestClient,
                              @Value("${oanda.account-id}") String accountId) {
        this.client = oandaRestClient;
        this.accountId = accountId;
    }

    public OandaDto.AccountSummary getAccountSummary() {
        OandaDto.AccountResponse response = client.get()
                .uri("/v3/accounts/{accountId}/summary", accountId)
                .retrieve()
                .body(OandaDto.AccountResponse.class);
        if (response == null || response.account() == null) {
            throw new IllegalStateException("OANDA returned an empty account summary");
        }
        return response.account();
    }

    public PriceQuote getPrice(String instrument) {
        OandaDto.PricingResponse response = client.get()
                .uri("/v3/accounts/{accountId}/pricing?instruments={instrument}", accountId, instrument)
                .retrieve()
                .body(OandaDto.PricingResponse.class);
        if (response == null || response.prices() == null || response.prices().isEmpty()) {
            throw new IllegalStateException("OANDA returned no price for " + instrument);
        }
        OandaDto.PriceDto p = response.prices().get(0);
        // When the market is closed the bid/ask lists can be empty; fall back to the closeout prices.
        String bid = firstPrice(p.bids(), p.closeoutBid());
        String ask = firstPrice(p.asks(), p.closeoutAsk());
        return new PriceQuote(p.instrument(), formatTime(p.time()), bid, ask, spread(bid, ask), p.tradeable());
    }

    public List<CandleRow> getCandles(String instrument, String granularity, int count) {
        OandaDto.CandlesResponse response = client.get()
                .uri("/v3/instruments/{instrument}/candles?granularity={granularity}&count={count}&price=M",
                        instrument, granularity, count)
                .retrieve()
                .body(OandaDto.CandlesResponse.class);
        if (response == null || response.candles() == null) {
            throw new IllegalStateException("OANDA returned no candles for " + instrument);
        }
        return response.candles().stream()
                .filter(c -> c.mid() != null)
                .map(c -> new CandleRow(formatTime(c.time()),
                        c.mid().open(), c.mid().high(), c.mid().low(), c.mid().close(),
                        c.volume(), c.complete()))
                .toList();
    }

    private static String firstPrice(List<OandaDto.Bucket> buckets, String fallback) {
        if (buckets != null && !buckets.isEmpty()) {
            return buckets.get(0).price();
        }
        return fallback != null ? fallback : "n/a";
    }

    private static String spread(String bid, String ask) {
        try {
            return new BigDecimal(ask).subtract(new BigDecimal(bid)).toPlainString();
        } catch (NumberFormatException e) {
            return "n/a";
        }
    }

    /** "2026-11-02T10:15:30.123456789Z" -> "2026-11-02 10:15:30 UTC" */
    static String formatTime(String time) {
        if (time == null) {
            return "n/a";
        }
        if (time.length() < 19) {
            return time;
        }
        return time.substring(0, 19).replace('T', ' ') + " UTC";
    }
}
