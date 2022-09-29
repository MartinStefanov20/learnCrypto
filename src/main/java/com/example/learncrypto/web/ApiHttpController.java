package com.example.learncrypto.web;

import com.example.learncrypto.model.Crypto;
import com.example.learncrypto.service.CryptoService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ApiHttpController {

    @Autowired
    HttpClient http;

    private String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";
    private static String apiKey = "8214b30e-1fa4-4b88-8302-76a2440ee75b";
    List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
    private final CryptoService cryptoService;


    public ApiHttpController(CryptoService cryptoService) {
        this.cryptoService = cryptoService;
        this.paratmers.add(new BasicNameValuePair("start","1"));
        this.paratmers.add(new BasicNameValuePair("limit","20"));
        this.paratmers.add(new BasicNameValuePair("convert","USD"));
    }

    @GetMapping("charts")
    public String charts(Model model) throws URISyntaxException, IOException, JSONException {
        String response_content = "";


        URIBuilder query = new URIBuilder(uri);
        query.addParameters(paratmers);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(query.build());

        request.setHeader(HttpHeaders.ACCEPT, "application/json");
        request.addHeader("X-CMC_PRO_API_KEY", apiKey);

        CloseableHttpResponse response = client.execute(request);

        try {
            //System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            response_content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        JSONObject json = new JSONObject(response_content);

//        JSONObject nesto = new JSONObject(json.get("data").toString());

        List<Crypto> cryptos = this.cryptoService.jsonToListCrypto(json);

        model.addAttribute("content", cryptos);
        return "charts";
    }

}
