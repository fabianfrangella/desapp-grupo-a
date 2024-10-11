package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoActiveBuilder;
import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.builder.TransactionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

public class CryptoUserTest {

    @Test
    public void whenMakeIntentionShouldAddTheIntentionToTheUser() {
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
    public void whenCancelIntentionShouldSetIntentionStatusToInactive() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.AAVEUSDT)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive))
                .build();
        var cryptoPrice = Mockito.mock(CryptoPrice.class);
        var intention = Mockito.spy(TradingIntentionBuilder.aTradingIntention().withId(1L).build());
        user.makeIntention(intention, cryptoPrice);

        user.cancelIntention(1L);

        Mockito.verify(intention, Mockito.times(1)).setStatus(TradingIntention.Status.INACTIVE);
    }

    @Test
    public void whenCancelNotExistentIntentionShouldThrowException() {
        var user = CryptoUserBuilder.aCryptoUser().build();

        Assertions.assertThrows(NoSuchTradingIntentionException.class, () -> user.cancelIntention(1L));

    }

    @Test
    public void whenDoCancelPenaltyUserShouldHave20lessReputation() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .build();
        user.doCancelPenalty();
        Assertions.assertEquals(80, user.getPoints());
    }

    @Test
    public void whenAddPointsShouldWorks() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .build();
        user.addPoints(20);
        Assertions.assertEquals(120, user.getPoints());
    }

    @Test
    public void whenAddBuyTransactionShouldWorks() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withBuyTransactions(HashSet.newHashSet(0))
                .withSellTransactions(HashSet.newHashSet(0))
                .build();

        var transaction = Mockito.mock(Transaction.class);

        user.addBuyTransaction(transaction);

        Assertions.assertEquals(1, user.getBuyTransactions().size());
        Assertions.assertEquals(0, user.getSellTransactions().size());
    }

    @Test
    public void whenAddSellTransactionShouldWorks() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withBuyTransactions(HashSet.newHashSet(0))
                .withSellTransactions(HashSet.newHashSet(0))
                .build();

        var transaction = Mockito.mock(Transaction.class);

        user.addSellTransaction(transaction);

        Assertions.assertEquals(0, user.getBuyTransactions().size());
        Assertions.assertEquals(1, user.getSellTransactions().size());
    }

    @Test
    public void whenAddQuantityShouldUpdateCryptoActive() {

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
    public void whenRemoveQuantityShouldUpdateCryptoActive() {

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
    public void whenAskNumberOperationsShouldReturnCorrectCount() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withBuyTransactions(new HashSet<>(Set.of(new Transaction(), new Transaction())))
                .withSellTransactions(new HashSet<>(Set.of(new Transaction())))
                .build();

        Long numberOfOperations = user.getNumberOperations();

        Assertions.assertEquals(3L, numberOfOperations);
    }
}