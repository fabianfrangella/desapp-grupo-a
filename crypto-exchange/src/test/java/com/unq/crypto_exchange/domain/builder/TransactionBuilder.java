package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction.TransactionStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class TransactionBuilder {

    private CryptoCurrencyType cryptoCurrency = CryptoCurrencyType.AAVEUSDT;
    private Long quantity = 10L;
    private CryptoPrice price = CryptoPrice.builder().build();
    private BigDecimal amount = BigDecimal.TEN;
    private CryptoUser seller = CryptoUserBuilder.aCryptoUser().withName("seller").build();
    private CryptoUser buyer = CryptoUserBuilder.aCryptoUser().withName("buyer").build();
    private OperationType operationType = OperationType.SALE;
    private TradingIntention tradingIntention;
    private TransactionStatus status = TransactionStatus.PENDING;
    private Instant createdAt = Instant.now();

    public static TransactionBuilder aTransaction() {
        return new TransactionBuilder();
    }

    public Transaction build() {
        Transaction transaction = new Transaction();
        transaction.setCryptoCurrency(cryptoCurrency);
        transaction.setQuantity(quantity);
        transaction.setPrice(price);
        transaction.setAmount(amount);
        transaction.setSeller(seller);
        transaction.setBuyer(buyer);
        transaction.setOperationType(operationType);
        transaction.setTradingIntention(tradingIntention);
        transaction.setStatus(status);
        transaction.setCreatedAt(createdAt);
        return transaction;
    }

    public TransactionBuilder withCryptoCurrency(final CryptoCurrencyType cryptoCurrency) {
        this.cryptoCurrency = cryptoCurrency;
        return this;
    }

    public TransactionBuilder withQuantity(final Long quantity) {
        this.quantity = quantity;
        return this;
    }

    public TransactionBuilder withPrice(final CryptoPrice price) {
        this.price = price;
        return this;
    }

    public TransactionBuilder withAmount(final BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public TransactionBuilder withSeller(final CryptoUser seller) {
        this.seller = seller;
        return this;
    }

    public TransactionBuilder withBuyer(final CryptoUser buyer) {
        this.buyer = buyer;
        return this;
    }

    public TransactionBuilder withOperationType(final OperationType operationType) {
        this.operationType = operationType;
        return this;
    }

    public TransactionBuilder withTradingIntention(final TradingIntention tradingIntention) {
        this.tradingIntention = tradingIntention;
        return this;
    }

    public TransactionBuilder withStatus(final TransactionStatus status) {
        this.status = status;
        return this;
    }

    public TransactionBuilder withCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}