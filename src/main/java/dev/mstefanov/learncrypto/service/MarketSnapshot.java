package dev.mstefanov.learncrypto.service;

import java.util.List;

/**
 * Market data shown on the charts page.
 *
 * @param coins  top coins by market cap
 * @param stale  {@code true} when the live request failed and the bundled snapshot is being served
 */
public record MarketSnapshot(List<MarketCoin> coins, boolean stale) {
}
