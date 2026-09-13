package dev.mstefanov.learncrypto.web;

import dev.mstefanov.learncrypto.model.Crypto;
import dev.mstefanov.learncrypto.service.CoinGeckoClient;
import dev.mstefanov.learncrypto.service.MarketSnapshot;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ChartsController {

    private final CoinGeckoClient coinGeckoClient;

    public ChartsController(CoinGeckoClient coinGeckoClient) {
        this.coinGeckoClient = coinGeckoClient;
    }

    @GetMapping("/charts")
    public String charts(Model model) {
        MarketSnapshot snapshot = coinGeckoClient.topMarkets();
        List<Crypto> rows = snapshot.coins().stream().map(Crypto::from).toList();

        model.addAttribute("content", rows);
        model.addAttribute("stale", snapshot.stale());
        model.addAttribute("asOf", snapshot.coins().isEmpty() ? null : snapshot.coins().get(0).lastUpdated());
        return "charts";
    }
}
