package com.unq.crypto_exchange.domain;


import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.exception.IllegalCancelOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class TradingIntentionTest {

    @Test
    @DisplayName("When Do Transaction With Status Inactive Should Fail")
    public void whenDoTransactionWithStatusInactiveShouldFail() {
        var tradingIntention = TradingIntentionBuilder.withInactiveStatus();
        Assertions.assertThrows(InactiveTradingIntentionException.class,
                () -> tradingIntention.doTransaction(CryptoUserBuilder.defaultCryptoUser()));
    }

    @Test
    @DisplayName("When Do Transaction With Cancel Operation Should Do Cancel Penalty")
    public void whenDoTransactionWithCancelOperationShouldDoCancelPenalty() {
        var user = Mockito.mock(CryptoUser.class);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.CANCEL);
        tradingIntention.doTransaction(user);
        Mockito.verify(user).doCancelPenalty();
    }

    @Test
    @DisplayName("When Do Transaction With Cancel Operation And Different Requester User Should Fail")
    public void whenDoTransactionWithCancelOperationAndDifferentRequesterUserShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.CANCEL);
        Assertions.assertThrows(IllegalCancelOperationException.class, () -> tradingIntention.doTransaction(requesterUser));
    }

    @Test
    @DisplayName("When Do Transaction With System Cancel Operation Should Do Nothing")
    public void whenDoTransactionWithSystemCancelOperationShouldDoNothing() {
        var user = Mockito.mock(CryptoUser.class);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.SYSTEM_CANCEL);
        tradingIntention.doTransaction(user);
        Mockito.verify(user, Mockito.times(0)).doCancelPenalty();
        Mockito.verify(user, Mockito.times(0)).doTransaction(Mockito.any());
    }

    @Test
    @DisplayName("When Do Transaction With Valid Data Should Do The Transaction")
    public void whenDoTransactionWithValidDataShouldDoTheTransaction() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.PURCHASE);
        tradingIntention.doTransaction(requesterUser);
        Mockito.verify(requesterUser).doTransaction(Mockito.any());
    }


}
