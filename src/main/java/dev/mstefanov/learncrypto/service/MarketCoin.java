package dev.mstefanov.learncrypto.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * One row of CoinGecko's {@code /coins/markets} response (only the fields the charts page needs).
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record MarketCoin(
        @JsonProperty("name") String name,
        @JsonProperty("symbol") String symbol,
        @JsonProperty("image") String image,
        @JsonProperty("current_price") Double currentPrice,
        @JsonProperty("market_cap") Double marketCap,
        @JsonProperty("price_change_percentage_1h_in_currency") Double priceChangePercentage1h,
        @JsonProperty("price_change_percentage_24h_in_currency") Double priceChangePercentage24h,
        @JsonProperty("price_change_percentage_7d_in_currency") Double priceChangePercentage7d,
        @JsonProperty("circulating_supply") Double circulatingSupply,
        @JsonProperty("last_updated") String lastUpdated
) {
}
