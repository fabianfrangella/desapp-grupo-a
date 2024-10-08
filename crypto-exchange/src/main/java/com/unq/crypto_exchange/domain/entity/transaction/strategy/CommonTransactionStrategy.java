package com.unq.crypto_exchange.domain.entity.transaction.strategy;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class CommonTransactionStrategy implements TransactionStrategy {
    @Override
    public void doTransaction(CryptoUser user, Transaction transaction) {
        var reputation = 5;
        if (Instant.now().isBefore(transaction.getTradingIntention().getCreatedAt().plus(30, ChronoUnit.MINUTES))) {
            reputation = 10;
        }
        transaction.confirm();
        transaction.getBuyer().addReputation(reputation);
        transaction.getSeller().addReputation(reputation);
    }

}
