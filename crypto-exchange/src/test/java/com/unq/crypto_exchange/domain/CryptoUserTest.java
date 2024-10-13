package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoActiveBuilder;
import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.builder.TransactionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

class CryptoUserTest {

    @Test
    void whenMakeIntentionShouldAddTheIntentionToTheUser() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.AAVEUSDT)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive)).build();

        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .build();

        var cryptoPrice = Mockito.mock(CryptoPrice.class);
        user.makeIntention(tradingIntention, cryptoPrice);
        Assertions.assertEquals(1, user.getIntentions().size());
    }

    @Test
    void whenDoCancelPenaltyUserShouldHave20lessReputation() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .build();
        user.doCancelPenalty();
        Assertions.assertEquals(80, user.getPoints());
    }

    @Test
    void whenAddPointsShouldWorks() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .build();
        user.addPoints(20);
        Assertions.assertEquals(120, user.getPoints());
    }

    @Test
    void whenAddQuantityShouldUpdateCryptoActive() {

        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.AAVEUSDT)
                .withQuantity(100L)
                .build();

        var cryptoUser = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive))
                .build();


        var transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getCryptoCurrency()).thenReturn(CryptoCurrencyType.AAVEUSDT);
        Mockito.when(transaction.getQuantity()).thenReturn(50L);

        cryptoUser.addQuantity(transaction);

        Assertions.assertEquals(150L, cryptoActive.getQuantity());
    }

    @Test
    void whenRemoveQuantityShouldUpdateCryptoActive() {

        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.AAVEUSDT)
                .withQuantity(100L)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive))
                .build();


        var transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getCryptoCurrency()).thenReturn(CryptoCurrencyType.AAVEUSDT);
        Mockito.when(transaction.getQuantity()).thenReturn(50L);

        user.removeQuantity(transaction);

        Assertions.assertEquals(50L, cryptoActive.getQuantity());
    }

    @Test
    void whenAskNumberOperationsShouldReturnCorrectCount() {
        var transaction = TransactionBuilder.aTransaction()
                .withStatus(Transaction.TransactionStatus.COMPLETED)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withBuyTransactions(new HashSet<>(Set.of(transaction)))
                .withSellTransactions(new HashSet<>(Set.of(transaction)))
                .build();

        Long numberOfOperations = user.getNumberOperations();

        Assertions.assertEquals(2L, numberOfOperations);
    }
}