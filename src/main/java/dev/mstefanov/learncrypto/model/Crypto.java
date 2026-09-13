package dev.mstefanov.learncrypto.model;

import dev.mstefanov.learncrypto.service.MarketCoin;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Row shown in the charts table (view model, not persisted).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Crypto {

    private String symbol;
    private String name;
    private String image;
    private Double price;
    private Double percent_change_1h;
    private Double percent_change_24h;
    private Double percent_change_7d;
    private Double circulating_supply;

    public static Crypto from(MarketCoin coin) {
        return new Crypto(
                coin.symbol() == null ? null : coin.symbol().toUpperCase(),
                coin.name(),
                coin.image(),
                coin.currentPrice(),
                coin.priceChangePercentage1h(),
                coin.priceChangePercentage24h(),
                coin.priceChangePercentage7d(),
                coin.circulatingSupply());
    }
}
