package dev.mstefanov.learncrypto.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    @GetMapping("/basics")
    public String basics() {
        return "basics";
    }

    @GetMapping("/earn-crypto")
    public String earnCrypto() {
        return "earn-crypto";
    }

    @GetMapping("/trade-crypto")
    public String tradeCrypto() {
        return "trade-crypto";
    }

    @GetMapping("/use-crypto")
    public String useCrypto() {
        return "use-crypto";
    }
}
