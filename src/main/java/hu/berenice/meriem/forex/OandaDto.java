package hu.berenice.meriem.forex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * The parts of the OANDA v20 JSON that we read. OANDA sends all prices and
 * balances as strings (e.g. "1.08345"), so we keep them as String.
 */
public final class OandaDto {

    private OandaDto() {
    }

    // GET /v3/accounts/{id}/summary
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AccountResponse(AccountSummary account) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AccountSummary(String id, String alias, String currency, String balance,
                                 @JsonProperty("NAV") String nav,
                                 String unrealizedPL, String pl,
                                 String marginUsed, String marginAvailable,
                                 int openTradeCount, int openPositionCount,
                                 String createdTime) {
    }

    // GET /v3/accounts/{id}/pricing?instruments=EUR_USD
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PricingResponse(List<PriceDto> prices) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record PriceDto(String instrument, String time, boolean tradeable,
                           List<Bucket> bids, List<Bucket> asks,
                           String closeoutBid, String closeoutAsk) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Bucket(String price, long liquidity) {
    }

    // GET /v3/instruments/{instrument}/candles?granularity=D&count=10&price=M
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CandlesResponse(String instrument, String granularity, List<CandleDto> candles) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CandleDto(boolean complete, long volume, String time, Ohlc mid) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Ohlc(@JsonProperty("o") String open,
                       @JsonProperty("h") String high,
                       @JsonProperty("l") String low,
                       @JsonProperty("c") String close) {
    }
}
