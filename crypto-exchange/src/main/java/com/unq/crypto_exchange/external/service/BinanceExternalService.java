package com.unq.crypto_exchange.external.service;

import com.unq.crypto_exchange.external.dto.BinancePriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@RequiredArgsConstructor
public class BinanceExternalService {

    private final WebClient binanceWebClient;

    public BinancePriceResponse getPrice(String symbol) {
        try {
            return binanceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ticker/price")
                            .queryParam("symbol", symbol)
                            .build())
                    .retrieve()
                    .bodyToMono(BinancePriceResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al consumir la API de Binance: " + e.getMessage(), e);
        }
    }
}