package com.unq.crypto_exchange.domain.entity;

public interface TransactionStrategy {
    public void doTransaction(CryptoUser user, Transaction transaction);
}
