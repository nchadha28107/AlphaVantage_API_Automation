package com.store.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlobalQuoteResponse {
    @JsonProperty("Global Quote")
    private GlobalQuote globalQuote;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class GlobalQuote {
        @JsonProperty("01. symbol")
        private String symbol;

        @JsonProperty("02. open")
        private String open;

        @JsonProperty("03. high")
        private String high;

        @JsonProperty("04. low")
        private String low;

        @JsonProperty("05. price")
        private String price;

        @JsonProperty("06. volume")
        private String volume;

        @JsonProperty("07. latest trading day")
        private String latestTradingDay;

        @JsonProperty("08. previous close")
        private String previousClose;

        @JsonProperty("09. change")
        private String change;

        @JsonProperty("10. change percent")
        private String changePercent;

        public boolean isEmpty() {
            return symbol == null && open == null && high == null && low == null
                    && price == null && volume == null && latestTradingDay == null
                    && previousClose == null && change == null && changePercent == null;
        }
    }
}