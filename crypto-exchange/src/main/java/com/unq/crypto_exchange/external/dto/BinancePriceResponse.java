package com.unq.crypto_exchange.external.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
public class BinancePriceResponse {
    private CryptoCurrencyType symbol;
    private BigDecimal price;

    public CryptoPrice toModel() {
        return CryptoPrice.builder()
                .cryptoCurrencyType(symbol)
                .price(price)
                .time(LocalDateTime.now())
                .build();
    }
}