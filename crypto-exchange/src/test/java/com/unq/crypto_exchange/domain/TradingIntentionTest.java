package com.unq.crypto_exchange.domain;


import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.exception.IllegalCancelOperationException;
import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
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
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(2L);
        Assertions.assertThrows(InactiveTradingIntentionException.class,
                () -> tradingIntention.doTransaction(requesterUser));
    }

    @Test
    @DisplayName("When Do Transaction With Cancel Operation And Different Requester User Should Fail")
    public void whenDoTransactionWithCancelOperationAndDifferentRequesterUserShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(2L);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.CANCEL);
        Assertions.assertThrows(IllegalCancelOperationException.class, () -> tradingIntention.doTransaction(requesterUser));
    }

    //TODO: Este creo que no va ma
/*    @Test
    @DisplayName("When Do Transaction With System Cancel Operation Should Do Nothing")
    public void whenDoTransactionWithSystemCancelOperationShouldDoNothing() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(2L);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.SYSTEM_CANCEL);
        tradingIntention.doTransaction(requesterUser);
        Mockito.verify(requesterUser, Mockito.times(0)).doCancelPenalty();
        Mockito.verify(requesterUser, Mockito.times(0)).doTransaction(Mockito.any());
    }*/

    @Test
    @DisplayName("When Do Transaction With Same Buyer and Seller User Should Fail")
    public void whenDoTransactionWithSameBuyerAndSellerUserShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(1L);
        var tradingIntention = TradingIntentionBuilder.withUserAndOperation(user, OperationType.PURCHASE);
        Assertions.assertThrows(IllegalOperationException.class, () -> tradingIntention.doTransaction(requesterUser));
    }


}
