package com.unq.crypto_exchange.domain.entity;



import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CommonTransactionStrategy implements TransactionStrategy {
    @Override
    public void doTransaction(CryptoUser user, Transaction transaction) {
        if (Instant.now().isAfter(transaction.getTradingIntention().getCreatedAt().plus(30, ChronoUnit.MINUTES))) {

        }
    }
}
