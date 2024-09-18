package com.unq.crypto_exchange.domain.entity;

public class BuyerUserStrategy extends CommonTransactionStrategy implements TransactionStrategy {
    @Override
    public void doTransaction(CryptoUser user, Transaction transaction) {
        super.doTransaction(user, transaction);
        user.addBuyTransaction(transaction);
        transaction.getSeller().addSellTransaction(transaction);
    }
}
