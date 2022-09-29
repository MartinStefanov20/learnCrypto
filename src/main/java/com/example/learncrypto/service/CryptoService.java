package com.example.learncrypto.service;

import com.example.learncrypto.model.Crypto;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class CryptoService {

    public List<Crypto> jsonToListCrypto(JSONObject json) throws JSONException {

        List<Crypto> list = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray("data");
        for(int i = 0; i<jsonArray.length(); i++){
            Crypto crypto = new Crypto();
            JSONObject object = jsonArray.getJSONObject(i);
            JSONObject quote = (JSONObject) object.get("quote");
            JSONObject USD = (JSONObject) quote.get("USD");
            crypto.setName((String) object.get("name"));
            crypto.setSymbol((String) object.get("symbol"));
            crypto.setPrice((Double) USD.get("price"));
            crypto.setPercent_change_1h((Double) USD.get("percent_change_1h"));
            crypto.setPercent_change_24h((Double) USD.get("percent_change_24h"));
            crypto.setPercent_change_7d((Double) USD.get("percent_change_7d"));
            crypto.setCirculating_supply(String.valueOf(object.get("circulating_supply")));
            list.add(crypto);
        }


        return list;
    }
}
