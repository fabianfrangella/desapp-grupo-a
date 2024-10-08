package com.unq.crypto_exchange.domain.entity.transaction.strategy;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;

public class SellerUserStrategy extends CommonTransactionStrategy implements TransactionStrategy {
    @Override
    public void doTransaction(CryptoUser user, Transaction transaction) {
        super.doTransaction(user, transaction);
        transaction.getSeller().addSellTransaction(transaction);
        transaction.getBuyer().addBuyTransaction(transaction);
    }
}
