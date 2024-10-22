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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
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

    @Test
    void whenFillInitialWalletShouldCreateCryptoActivesForAllTypes() {
        var user = CryptoUserBuilder.aCryptoUser().build();

        user.fillInitialWallet();

        Assertions.assertEquals(CryptoCurrencyType.values().length, user.getCryptoActives().size());
        user.getCryptoActives().forEach(cryptoActive ->
                Assertions.assertEquals(0L, cryptoActive.getQuantity()));
    }

    @Test
    void whenAskForReputationShouldReturnZeroIfNoOperations() {
        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .build();

        Assertions.assertEquals(BigDecimal.ZERO, user.getReputation());
    }

    @Test
    void whenAskForReputationShouldCalculateCorrectly() {
        var transaction = Mockito.mock(Transaction.class);
        Mockito.when(transaction.getStatus()).thenReturn(Transaction.TransactionStatus.COMPLETED);

        var user = CryptoUserBuilder.aCryptoUser()
                .withPoints(100)
                .withBuyTransactions(Set.of(transaction))
                .withSellTransactions(Set.of(transaction))
                .build();

        var expectedReputation = BigDecimal.valueOf(50.00).setScale(2, RoundingMode.HALF_UP); // 100 points / 2 operations
        Assertions.assertEquals(expectedReputation, user.getReputation());
    }

    @Test
    void whenFindCryptoActivesOperatedBetweenShouldReturnCorrectList() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.BTCUSDT)
                .build();

        var transaction = TransactionBuilder.aTransaction()
                .withCreatedAt(Instant.now())
                .withCryptoCurrency(CryptoCurrencyType.BTCUSDT)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive))
                .withBuyTransactions(Set.of(transaction))
                .build();

        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);

        var result = user.findCryptoActivesOperatedBetween(from, to);

        Assertions.assertTrue(result.contains(cryptoActive));
    }

    @Test
    void whenFindCryptoActivesOperatedBetweenShouldReturnEmptyIfNoMatches() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withType(CryptoCurrencyType.ETHUSDT)
                .build();

        var transaction = TransactionBuilder.aTransaction()
                .withCreatedAt(Instant.now())
                .withCryptoCurrency(CryptoCurrencyType.BTCUSDT)
                .build();

        var user = CryptoUserBuilder.aCryptoUser()
                .withCryptoActives(Set.of(cryptoActive))
                .withBuyTransactions(Set.of(transaction))
                .build();

        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = LocalDate.now().plusDays(1);

        var result = user.findCryptoActivesOperatedBetween(from, to);

        Assertions.assertTrue(result.isEmpty());
    }
}