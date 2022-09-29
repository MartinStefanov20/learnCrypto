package com.example.learncrypto.web;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.stereotype.Component;


@Component
public class HttpClient {

    private org.apache.http.client.HttpClient client = HttpClients.custom().build();

    private Header JSON[] = {
            new BasicHeader("Content-type", "application/json"),
            new BasicHeader("Accept", "application/json")
    };


    public HttpResponse get(String url) throws IOException {
        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeaders(JSON);
        return client.execute(getRequest);
    }


}