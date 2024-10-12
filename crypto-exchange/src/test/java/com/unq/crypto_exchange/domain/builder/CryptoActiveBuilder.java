package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoActive;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoUser;

public class CryptoActiveBuilder {
    private CryptoUser user;
    private CryptoCurrencyType type = CryptoCurrencyType.AAVEUSDT;
    private Long quantity = 0L;

    public static CryptoActiveBuilder aCryptoActive() {
        return new CryptoActiveBuilder();
    }

    public CryptoActive build() {
        CryptoActive cryptoActive = new CryptoActive();
        cryptoActive.setUser(user);
        cryptoActive.setType(type);
        cryptoActive.setQuantity(quantity);
        return cryptoActive;
    }

    public CryptoActiveBuilder withUser(final CryptoUser user) {
        this.user = user;
        return this;
    }

    public CryptoActiveBuilder withType(final CryptoCurrencyType type) {
        this.type = type;
        return this;
    }

    public CryptoActiveBuilder withQuantity(final Long quantity) {
        this.quantity = quantity;
        return this;
    }
}