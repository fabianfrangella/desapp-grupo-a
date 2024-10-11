package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoActive;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;

public class CryptoActiveBuilder {
    public static CryptoActive defaultCryptoActive() {
        return CryptoActive.builder()
                .type(CryptoCurrencyType.AAVEUSDT)
                .quantity(100L)
                .build();
    }
}
