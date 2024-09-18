package com.unq.crypto_exchange.domain.entity.transaction.strategy;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;

public interface TransactionStrategy {
    public void doTransaction(CryptoUser user, Transaction transaction);
}
