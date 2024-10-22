package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.*;

import java.math.BigDecimal;
import java.time.Instant;

public class TradingIntentionBuilder {

    private CryptoCurrencyType cryptoCurrencyType = CryptoCurrencyType.AAVEUSDT;
    private Long quantity = 0L;
    private CryptoPrice price;
    private BigDecimal amount = BigDecimal.ZERO;
    private CryptoUser user;
    private OperationType operationType;
    private TradingIntention.Status status = TradingIntention.Status.ACTIVE;
    private Long id = 1L;
    private Instant createdAt;

    public static TradingIntentionBuilder aTradingIntention() {
        return new TradingIntentionBuilder();
    }

    public TradingIntention build() {
        TradingIntention tradingIntention = new TradingIntention();
        tradingIntention.setCryptoCurrencyType(cryptoCurrencyType);
        tradingIntention.setQuantity(quantity);
        tradingIntention.setPrice(price);
        tradingIntention.setAmount(amount);
        tradingIntention.setUser(user);
        tradingIntention.setOperationType(operationType);
        tradingIntention.setStatus(status);
        tradingIntention.setId(id);
        tradingIntention.setCreatedAt(createdAt);
        return tradingIntention;
    }

    public TradingIntentionBuilder withCryptoCurrencyType(final CryptoCurrencyType cryptoCurrencyType) {
        this.cryptoCurrencyType = cryptoCurrencyType;
        return this;
    }

    public TradingIntentionBuilder withQuantity(final Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public TradingIntentionBuilder withPrice(final CryptoPrice price) {
        this.price = price;
        return this;
    }

    public TradingIntentionBuilder withAmount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TradingIntentionBuilder withUser(final CryptoUser user) {
        this.user = user;
        return this;
    }

    public TradingIntentionBuilder withOperationType(final OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public TradingIntentionBuilder withStatus(final TradingIntention.Status status) {
        this.status = status;
        return this;
    }

    public TradingIntentionBuilder withId(final Long id) {
        this.id = id;
        return this;
    }

    public TradingIntentionBuilder withCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}