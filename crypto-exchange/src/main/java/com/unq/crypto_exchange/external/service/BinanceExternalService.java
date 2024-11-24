package com.unq.crypto_exchange.external.service;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.external.dto.BinancePriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<BinancePriceResponse> getPrices(List<CryptoCurrencyType> symbols) {
        try {

            String formattedSymbols = symbols.stream()
                    .map(Enum::name)
                    .map(symbol -> "\"" + symbol + "\"")
                    .collect(Collectors.joining(",", "[", "]"));

            return binanceWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/ticker/price")
                            .queryParam("symbols", formattedSymbols)
                            .build())
                    .retrieve()
                    .bodyToFlux(BinancePriceResponse.class)
                    .collectList()
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al consumir la API de Binance: " + e.getMessage(), e);
        }
    }
}