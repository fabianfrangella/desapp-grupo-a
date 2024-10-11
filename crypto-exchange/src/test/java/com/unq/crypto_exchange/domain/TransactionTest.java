package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TransactionBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction.TransactionStatus;
import com.unq.crypto_exchange.domain.entity.transaction.TransactionAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TransactionTest {

    @Test
    public void whenTransactionIsCompletedShouldThrowException() {
        var transaction = TransactionBuilder.aTransaction()
                .withStatus(TransactionStatus.COMPLETED)
                .build();

        Assertions.assertThrows(IllegalOperationException.class, () -> transaction.process(TransactionAction.CONFIRM));
    }

    @Test
    public void whenTransactionIsCanceledShouldThrowException() {
        var transaction = TransactionBuilder.aTransaction()
                .withStatus(TransactionStatus.CANCELED)
                .build();

        Assertions.assertThrows(IllegalOperationException.class, () -> transaction.process(TransactionAction.CONFIRM));
    }
    
    @Test
    public void whenTradingIntentionIsInactiveShouldThrowException() {
        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withStatus(TradingIntention.Status.INACTIVE)
                .build();

        var transaction = TransactionBuilder.aTransaction()
                .withTradingIntention(tradingIntention)
                .build();

        Assertions.assertThrows(InactiveTradingIntentionException.class, () -> transaction.process(TransactionAction.CONFIRM));
    }

    @Test
    public void whenCancelTransactionShouldApplyPenalties() {
        var buyer = Mockito.mock(CryptoUser.class);
        var seller = Mockito.mock(CryptoUser.class);
        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withStatus(TradingIntention.Status.ACTIVE)
                .withUser(buyer)
                .build();

        var transaction = TransactionBuilder.aTransaction()
                .withBuyer(buyer)
                .withSeller(seller)
                .withTradingIntention(tradingIntention)
                .build();

        transaction.process(TransactionAction.CANCEL);

        Assertions.assertEquals(TransactionStatus.CANCELED, transaction.getStatus());
        Mockito.verify(tradingIntention.getUser(), Mockito.times(1)).doCancelPenalty();
        Mockito.verify(buyer, Mockito.times(1)).removeQuantity(transaction);
        Mockito.verify(seller, Mockito.times(1)).addQuantity(transaction);
    }

    @Test
    public void whenConfirmTransactionWithin30MinutesShouldAdd10Points() {
        var buyer = Mockito.mock(CryptoUser.class);
        var seller = Mockito.mock(CryptoUser.class);
        var tradingIntention = Mockito.mock(TradingIntention.class);

        Mockito.when(tradingIntention.getStatus()).thenReturn(TradingIntention.Status.ACTIVE);
        Mockito.when(tradingIntention.getCreatedAt()).thenReturn(Instant.now().minus(10, ChronoUnit.MINUTES));

        var transaction = TransactionBuilder.aTransaction()
                .withBuyer(buyer)
                .withSeller(seller)
                .withTradingIntention(tradingIntention)
                .build();

        transaction.process(TransactionAction.CONFIRM);

        Assertions.assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        Mockito.verify(buyer, Mockito.times(1)).addPoints(10);
        Mockito.verify(seller, Mockito.times(1)).addPoints(10);
        Mockito.verify(tradingIntention, Mockito.times(1)).setStatus(TradingIntention.Status.INACTIVE);
    }

    @Test
    public void whenConfirmTransactionAfter30MinutesShouldAdd5Points() {
        var buyer = Mockito.mock(CryptoUser.class);
        var seller = Mockito.mock(CryptoUser.class);
        var tradingIntention = Mockito.mock(TradingIntention.class);

        Mockito.when(tradingIntention.getStatus()).thenReturn(TradingIntention.Status.ACTIVE);
        Mockito.when(tradingIntention.getCreatedAt()).thenReturn(Instant.now().minus(40, ChronoUnit.MINUTES));

        var transaction = TransactionBuilder.aTransaction()
                .withBuyer(buyer)
                .withSeller(seller)
                .withTradingIntention(tradingIntention)
                .build();

        transaction.process(TransactionAction.CONFIRM);

        Assertions.assertEquals(TransactionStatus.COMPLETED, transaction.getStatus());
        Mockito.verify(buyer, Mockito.times(1)).addPoints(5);
        Mockito.verify(seller, Mockito.times(1)).addPoints(5);
        Mockito.verify(tradingIntention, Mockito.times(1)).setStatus(TradingIntention.Status.INACTIVE);
    }

}
