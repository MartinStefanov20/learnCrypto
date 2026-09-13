package dev.mstefanov.learncrypto.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.List;

/**
 * Fetches the top coins by market cap from the public CoinGecko API.
 * <p>
 * Results are cached for 90 seconds (Caffeine cache "markets"). If the upstream call fails for any
 * reason the bundled snapshot {@code data/markets-fallback.json} is returned with {@code stale=true};
 * stale results are not cached so the next request tries the live API again.
 */
@Service
public class CoinGeckoClient {

    private static final Logger log = LoggerFactory.getLogger(CoinGeckoClient.class);
    private static final String FALLBACK_RESOURCE = "data/markets-fallback.json";
    private static final ParameterizedTypeReference<List<MarketCoin>> COIN_LIST =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public CoinGeckoClient(RestClient.Builder restClientBuilder,
                           ObjectMapper objectMapper,
                           @Value("${coingecko.base-url}") String baseUrl,
                           @Value("${coingecko.api-key:}") String apiKey) {
        RestClient.Builder builder = restClientBuilder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/json");
        if (StringUtils.hasText(apiKey)) {
            builder.defaultHeader("x-cg-demo-api-key", apiKey);
        }
        this.restClient = builder.build();
        this.objectMapper = objectMapper;
    }

    @Cacheable(cacheNames = "markets", unless = "#result.stale")
    public MarketSnapshot topMarkets() {
        try {
            List<MarketCoin> coins = restClient.get()
                    .uri(uri -> uri.path("/coins/markets")
                            .queryParam("vs_currency", "usd")
                            .queryParam("order", "market_cap_desc")
                            .queryParam("per_page", 20)
                            .queryParam("page", 1)
                            .queryParam("price_change_percentage", "1h,24h,7d")
                            .build())
                    .retrieve()
                    .body(COIN_LIST);
            if (coins == null || coins.isEmpty()) {
                throw new IllegalStateException("CoinGecko returned no coins");
            }
            return new MarketSnapshot(coins, false);
        } catch (RuntimeException ex) {
            log.warn("CoinGecko request failed ({}); serving bundled snapshot", ex.getMessage());
            return new MarketSnapshot(loadFallback(), true);
        }
    }

    List<MarketCoin> loadFallback() {
        try (InputStream in = new ClassPathResource(FALLBACK_RESOURCE).getInputStream()) {
            return objectMapper.readValue(in, new TypeReference<List<MarketCoin>>() {
            });
        } catch (IOException ex) {
            throw new UncheckedIOException("Cannot read " + FALLBACK_RESOURCE, ex);
        }
    }
}
