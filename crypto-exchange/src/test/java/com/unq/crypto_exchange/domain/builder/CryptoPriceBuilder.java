package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CryptoPriceBuilder {

    private Long id;
    private CryptoCurrencyType cryptoCurrencyType = CryptoCurrencyType.AAVEUSDT;
    private LocalDateTime time = LocalDateTime.now();
    private BigDecimal price = BigDecimal.valueOf(100.00);

    public static CryptoPriceBuilder aCryptoPrice() {
        return new CryptoPriceBuilder();
    }

    public CryptoPrice build() {
        CryptoPrice cryptoPrice = new CryptoPrice();
        cryptoPrice.setId(id);
        cryptoPrice.setCryptoCurrencyType(cryptoCurrencyType);
        cryptoPrice.setTime(time);
        cryptoPrice.setPrice(price);
        return cryptoPrice;
    }

    public CryptoPriceBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public CryptoPriceBuilder withCryptoCurrencyType(final CryptoCurrencyType cryptoCurrencyType) {
        this.cryptoCurrencyType = cryptoCurrencyType;
        return this;
    }

    public CryptoPriceBuilder withTime(final LocalDateTime time) {
        this.time = time;
        return this;
    }

    public CryptoPriceBuilder withPrice(final BigDecimal price) {
        this.price = price;
        return this;
    }
}