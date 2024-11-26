package com.unq.crypto_exchange.external.service;

import com.unq.crypto_exchange.external.dto.CryptoDolarResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
@AllArgsConstructor
public class DolarapiExternalService {

    private final WebClient dolarapiWebClient;

    public CryptoDolarResponse getCryptoDollar() {
        try {
            return dolarapiWebClient.get()
                    .uri("/v1/dolares/cripto")
                    .retrieve()
                    .bodyToMono(CryptoDolarResponse.class)
                    .block();
        } catch (WebClientResponseException e) {
            throw new RuntimeException("Error al consumir la API de Dolarapi: " + e.getMessage(), e);
        }
    }

}
