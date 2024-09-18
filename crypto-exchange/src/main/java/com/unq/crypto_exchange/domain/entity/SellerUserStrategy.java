package com.unq.crypto_exchange.domain.entity;

public class SellerUserStrategy extends CommonTransactionStrategy implements TransactionStrategy {
    @Override
    public void doTransaction(CryptoUser user, Transaction transaction) {
        super.doTransaction(user, transaction);
        user.addSellTransaction(transaction);
        transaction.getBuyer().addBuyTransaction(transaction);
    }
}
