package com.unq.crypto_exchange.domain;


import com.unq.crypto_exchange.domain.builder.CryptoPriceBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.exception.IllegalCancelOperationException;
import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;

class TradingIntentionTest {

    @Test
    @DisplayName("When Do Transaction With Status Inactive Should Fail")
    void whenDoTransactionWithStatusInactiveShouldFail() {
        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withStatus(TradingIntention.Status.INACTIVE)
                .build();
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(2L);
        Assertions.assertThrows(InactiveTradingIntentionException.class,
                () -> tradingIntention.createTransaction(requesterUser));
    }

    @Test
    @DisplayName("When Do Transaction With Cancel Operation And Different Requester User Should Fail")
    void whenDoTransactionWithCancelOperationAndDifferentRequesterUserShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(2L);
        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withUser(user)
                .withOperationType(OperationType.CANCEL)
                .build();
        Assertions.assertThrows(IllegalCancelOperationException.class, () -> tradingIntention.createTransaction(requesterUser));
    }

    @Test
    @DisplayName("When Do Transaction With Same Buyer and Seller User Should Fail")
    void whenDoTransactionWithSameBuyerAndSellerUserShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        var requesterUser = Mockito.mock(CryptoUser.class);
        Mockito.when(user.getId()).thenReturn(1L);
        Mockito.when(requesterUser.getId()).thenReturn(1L);
        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withUser(user)
                .withOperationType(OperationType.PURCHASE)
                .build();
        Assertions.assertThrows(IllegalOperationException.class, () -> tradingIntention.createTransaction(requesterUser));
    }

    @Test
    @DisplayName("When Amount Is Out of Five Percent Range Should Inactivate Trading Intention and Cancel Transaction")
    void whenAmountIsOutOfFivePercentRangeShouldInactivateTradingIntentionAndCancelTransaction() {
        var user = Mockito.mock(CryptoUser.class);

        var price = CryptoPriceBuilder.aCryptoPrice()
                .withPrice(new BigDecimal("100.00"))
                .build();

        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withPrice(price)
                .withUser(user)
                .withAmount(new BigDecimal("110.00"))
                .withStatus(TradingIntention.Status.ACTIVE)
                .withOperationType(OperationType.PURCHASE)
                .build();

        var requestUser = Mockito.mock(CryptoUser.class);
        Mockito.when(requestUser.getId()).thenReturn(2L);
        Mockito.when(requestUser.hasEnoughQuantity(tradingIntention)).thenReturn(true);

        var transaction = tradingIntention.createTransaction(requestUser);

        Assertions.assertEquals(Transaction.TransactionStatus.CANCELED, transaction.getStatus());
        Assertions.assertEquals(TradingIntention.Status.INACTIVE, tradingIntention.getStatus());
    }

    @Test
    @DisplayName("When Seller Does Not Have Enough Quantity Should Fail")
    void whenSellerDoesNotHaveEnoughQuantityShouldFail() {
        var user = Mockito.mock(CryptoUser.class);
        Mockito.when(user.hasEnoughQuantity(Mockito.any())).thenReturn(false);

        var tradingIntention = TradingIntentionBuilder.aTradingIntention()
                .withUser(user)
                .withOperationType(OperationType.SALE)
                .build();

        var requestUser = Mockito.mock(CryptoUser.class);

        Assertions.assertThrows(IllegalOperationException.class, () -> tradingIntention.createTransaction(requestUser));
    }

}
