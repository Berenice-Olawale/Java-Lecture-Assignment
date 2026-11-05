package hu.berenice.meriem.forex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

public final class OandaTradeDto {

    private OandaTradeDto() {
    }

    // POST /v3/accounts/{id}/orders   and   PUT /v3/accounts/{id}/trades/{tradeId}/close
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OrderResponse(FillTransaction orderFillTransaction,
                                CancelTransaction orderCancelTransaction,
                                RejectTransaction orderRejectTransaction) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FillTransaction(String id, String instrument, String units, String price,
                                  String pl, String time,
                                  TradeOpened tradeOpened, List<TradeClosed> tradesClosed) {
    }

    /** The real trade ID is here (the fill transaction has its own, different ID). */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TradeOpened(String tradeID, String units) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TradeClosed(String tradeID, String units, String realizedPL, String price) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record CancelTransaction(String id, String reason) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record RejectTransaction(String id, String rejectReason) {
    }

    // GET /v3/accounts/{id}/openTrades
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TradesResponse(List<TradeDto> trades) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TradeDto(String id, String instrument, String price, String openTime,
                           String currentUnits, String unrealizedPL, String state) {
    }
}