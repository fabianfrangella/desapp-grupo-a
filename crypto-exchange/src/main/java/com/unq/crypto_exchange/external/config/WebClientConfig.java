package com.unq.crypto_exchange.external.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${binance.api.base.url}")
    private String binanceBaseUrl;

    @Value("${dolarapi.api.base.url}")
    private String dolarapiBaseUrl;

    @Bean(name = "binanceWebClient")
    public WebClient binanceWebClient(WebClient.Builder builder) {
        return builder.baseUrl(binanceBaseUrl).build();
    }

    @Bean(name = "dolarapiWebClient")
    public WebClient dolarapiWebClient(WebClient.Builder builder) {
        return builder.baseUrl(dolarapiBaseUrl).build();
    }
}

