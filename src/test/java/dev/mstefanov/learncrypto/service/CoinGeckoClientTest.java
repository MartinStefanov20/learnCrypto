package dev.mstefanov.learncrypto.service;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.headerDoesNotExist;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Slice test of {@link CoinGeckoClient}: the {@code RestClient.Builder} is bound to a
 * {@link MockRestServiceServer}, so no network access happens. Caching is not active in this slice.
 */
@RestClientTest(CoinGeckoClient.class)
@TestPropertySource(properties = {
        "coingecko.base-url=https://coingecko.test/api/v3",
        "coingecko.api-key="
})
class CoinGeckoClientTest {

    private static final String MARKETS_URL = "https://coingecko.test/api/v3/coins/markets";

    private static final String TWO_COINS = """
            [
              {"id":"bitcoin","symbol":"btc","name":"Bitcoin",
               "image":"https://img.test/btc.png","current_price":77227.0,"market_cap":1551005105430,
               "price_change_percentage_1h_in_currency":0.1,"price_change_percentage_24h_in_currency":-1.5,
               "price_change_percentage_7d_in_currency":4.2,"circulating_supply":20083837.0,
               "last_updated":"2026-09-13T19:49:20.000Z","unknown_field":"ignored"},
              {"id":"ethereum","symbol":"eth","name":"Ethereum",
               "image":"https://img.test/eth.png","current_price":2350.5,"market_cap":283000000000,
               "last_updated":"2026-09-13T19:49:21.000Z"}
            ]
            """;

    @Autowired
    private CoinGeckoClient client;

    @Autowired
    private MockRestServiceServer server;

    @Test
    void mapsMarketsResponseToCoins() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(MARKETS_URL)))
                .andExpect(method(HttpMethod.GET))
                .andExpect(queryParam("vs_currency", "usd"))
                .andExpect(queryParam("order", "market_cap_desc"))
                .andExpect(queryParam("per_page", "20"))
                .andExpect(header("Accept", "application/json"))
                .andExpect(headerDoesNotExist("x-cg-demo-api-key"))
                .andRespond(withSuccess(TWO_COINS, MediaType.APPLICATION_JSON));

        MarketSnapshot snapshot = client.topMarkets();

        server.verify();
        assertThat(snapshot.stale()).isFalse();
        assertThat(snapshot.coins()).hasSize(2);

        MarketCoin btc = snapshot.coins().get(0);
        assertThat(btc.name()).isEqualTo("Bitcoin");
        assertThat(btc.symbol()).isEqualTo("btc");
        assertThat(btc.currentPrice()).isEqualTo(77227.0);
        assertThat(btc.marketCap()).isEqualTo(1_551_005_105_430.0);
        assertThat(btc.priceChangePercentage1h()).isEqualTo(0.1);
        assertThat(btc.priceChangePercentage24h()).isEqualTo(-1.5);
        assertThat(btc.priceChangePercentage7d()).isEqualTo(4.2);
        assertThat(btc.circulatingSupply()).isEqualTo(20_083_837.0);
        assertThat(btc.lastUpdated()).isEqualTo("2026-09-13T19:49:20.000Z");

        MarketCoin eth = snapshot.coins().get(1);
        assertThat(eth.name()).isEqualTo("Ethereum");
        assertThat(eth.priceChangePercentage7d()).isNull();
    }

    @Test
    void serverErrorFallsBackToBundledSnapshot() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(MARKETS_URL)))
                .andRespond(withServerError());

        MarketSnapshot snapshot = client.topMarkets();

        server.verify();
        assertThat(snapshot.stale()).isTrue();
        assertThat(snapshot.coins()).isNotEmpty();
        assertThat(snapshot.coins().get(0).name()).isEqualTo("Bitcoin");
        assertThat(snapshot.coins()).allSatisfy(coin -> {
            assertThat(coin.name()).isNotBlank();
            assertThat(coin.currentPrice()).isNotNull();
        });
    }

    @Test
    void emptyBodyIsTreatedAsFailure() {
        server.expect(requestTo(org.hamcrest.Matchers.startsWith(MARKETS_URL)))
                .andRespond(withSuccess("[]", MediaType.APPLICATION_JSON));

        MarketSnapshot snapshot = client.topMarkets();

        assertThat(snapshot.stale()).isTrue();
        assertThat(snapshot.coins()).isNotEmpty();
    }

    @Nested
    @TestPropertySource(properties = "coingecko.api-key=demo-key-123")
    class WithDemoApiKey {

        @Autowired
        private CoinGeckoClient keyedClient;

        @Autowired
        private MockRestServiceServer keyedServer;

        @Test
        void addsDemoApiKeyHeaderWhenConfigured() {
            keyedServer.expect(requestTo(org.hamcrest.Matchers.startsWith(MARKETS_URL)))
                    .andExpect(header("x-cg-demo-api-key", "demo-key-123"))
                    .andRespond(withSuccess(TWO_COINS, MediaType.APPLICATION_JSON));

            MarketSnapshot snapshot = keyedClient.topMarkets();

            keyedServer.verify();
            assertThat(snapshot.stale()).isFalse();
            assertThat(snapshot.coins()).hasSize(2);
        }
    }
}
