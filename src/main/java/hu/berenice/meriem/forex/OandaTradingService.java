package hu.berenice.meriem.forex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class OandaTradingService {

    public record OpenResult(String tradeId, String instrument, String units, String price, String time) {
    }


    public record TradeRow(String id, String instrument, String openTime, String units,
                           String price, String unrealizedPL, boolean isLong) {
    }

    public record CloseResult(String tradeId, String units, String price, String realizedPL) {
    }

    private static final Pattern ERROR_MESSAGE = Pattern.compile("\"errorMessage\"\\s*:\\s*\"([^\"]*)\"");

    private final RestClient client;
    private final String accountId;

    public OandaTradingService(RestClient oandaRestClient,
                               @Value("${oanda.account-id}") String accountId) {
        this.client = oandaRestClient;
        this.accountId = accountId;
    }


    public OpenResult openPosition(String instrument, int units) {
        Map<String, Object> body = Map.of("order", Map.of(
                "type", "MARKET",
                "instrument", instrument,
                "units", String.valueOf(units),
                "timeInForce", "FOK",
                "positionFill", "DEFAULT"));
        try {
            OandaTradeDto.OrderResponse response = client.post()
                    .uri("/v3/accounts/{accountId}/orders", accountId)
                    .body(body)
                    .retrieve()
                    .body(OandaTradeDto.OrderResponse.class);

            if (response == null) {
                throw new IllegalStateException("OANDA returned an empty answer");
            }
            OandaTradeDto.FillTransaction fill = response.orderFillTransaction();
            if (fill == null) {
                throw new IllegalStateException("The order was not filled: " + failureReason(response));
            }
            // If the order only reduced an existing opposite trade, no new trade is opened.
            String tradeId = fill.tradeOpened() != null ? fill.tradeOpened().tradeID() : "none (an existing trade was reduced)";
            return new OpenResult(tradeId, fill.instrument(), fill.units(), fill.price(),
                    OandaMarketService.formatTime(fill.time()));
        } catch (RestClientResponseException e) {
            throw new IllegalStateException(describe(e), e);
        }
    }

    public List<TradeRow> listOpenTrades() {
        try {
            OandaTradeDto.TradesResponse response = client.get()
                    .uri("/v3/accounts/{accountId}/openTrades", accountId)
                    .retrieve()
                    .body(OandaTradeDto.TradesResponse.class);
            if (response == null || response.trades() == null) {
                return List.of();
            }
            return response.trades().stream()
                    .map(t -> new TradeRow(t.id(), t.instrument(),
                            OandaMarketService.formatTime(t.openTime()),
                            t.currentUnits(), t.price(), t.unrealizedPL(),
                            !t.currentUnits().startsWith("-")))
                    .toList();
        } catch (RestClientResponseException e) {
            throw new IllegalStateException(describe(e), e);
        }
    }
    public CloseResult closeTrade(long tradeId) {
        try {
            OandaTradeDto.OrderResponse response = client.put()
                    .uri("/v3/accounts/{accountId}/trades/{tradeId}/close", accountId, tradeId)
                    .body(Map.of("units", "ALL"))
                    .retrieve()
                    .body(OandaTradeDto.OrderResponse.class);

            if (response == null) {
                throw new IllegalStateException("OANDA returned an empty answer");
            }
            OandaTradeDto.FillTransaction fill = response.orderFillTransaction();
            if (fill == null) {
                throw new IllegalStateException("The trade was not closed: " + failureReason(response));
            }
            String units = fill.units();
            String realizedPL = fill.pl();
            if (fill.tradesClosed() != null && !fill.tradesClosed().isEmpty()) {
                units = fill.tradesClosed().get(0).units();
                realizedPL = fill.tradesClosed().get(0).realizedPL();
            }
            return new CloseResult(String.valueOf(tradeId), units, fill.price(), realizedPL);
        } catch (RestClientResponseException e) {
            throw new IllegalStateException(describe(e), e);
        }
    }

    private static String failureReason(OandaTradeDto.OrderResponse response) {
        if (response.orderCancelTransaction() != null) {
            return response.orderCancelTransaction().reason();
        }
        if (response.orderRejectTransaction() != null) {
            return response.orderRejectTransaction().rejectReason();
        }
        return "unknown reason";
    }

    private static String describe(RestClientResponseException e) {
        Matcher m = ERROR_MESSAGE.matcher(e.getResponseBodyAsString());
        String detail = m.find() ? m.group(1) : e.getStatusText();
        return "OANDA answered " + e.getStatusCode().value() + ": " + detail;
    }
}