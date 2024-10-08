package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;

public class CryptoUserTest {

    @Test
    public void whenMakeIntentionShouldAddTheIntentionToTheUser() {
        var user = CryptoUserBuilder.defaultCryptoUser();
        var cryptoPrice = Mockito.mock(CryptoPrice.class);
        user.makeIntention(TradingIntentionBuilder.withDefaultData(), cryptoPrice);
        Assertions.assertEquals(1, user.getIntentions().size());
    }

    @Test
    public void whenCancelIntentionShouldSetIntentionStatusToInactive() {
        var user = CryptoUserBuilder.defaultCryptoUser();
        var cryptoPrice = Mockito.mock(CryptoPrice.class);
        var intention = Mockito.spy(TradingIntentionBuilder.withId(1L));
        user.makeIntention(intention, cryptoPrice);

        user.cancelIntention(1L);

        Mockito.verify(intention, Mockito.times(1)).setStatus(TradingIntention.Status.INACTIVE);
    }

    @Test
    public void whenCancelNotExistentIntentionShouldThrowException() {
        var user = CryptoUserBuilder.defaultCryptoUser();

        Assertions.assertThrows(NoSuchTradingIntentionException.class, () -> user.cancelIntention(1L));

    }

    @Test
    public void whenDoTransactionAsBuyerShouldAddTransactionToBuyerAndSellerAndAddReputationToBoth() {
        var buyer = CryptoUserBuilder.defaultCryptoUser();
        var seller = CryptoUserBuilder.defaultCryptoUser();
        var transaction = Mockito.mock(Transaction.class);
        var intention = Mockito.mock(TradingIntention.class);

        Mockito.when(transaction.getBuyer()).thenReturn(buyer);
        Mockito.when(transaction.getSeller()).thenReturn(seller);
        Mockito.when(transaction.getTradingIntention()).thenReturn(intention);
        Mockito.when(intention.getCreatedAt()).thenReturn(Instant.MIN);

        buyer.doTransaction(transaction);

        Assertions.assertEquals(1, buyer.getBuyTransactions().size());
        Assertions.assertEquals(1, seller.getSellTransactions().size());
        Assertions.assertEquals(5,buyer.getReputation());
        Assertions.assertEquals(5,seller.getReputation());
    }

    @Test
    public void whenDoTransactionAsSellerShouldAddTransactionToBuyerAndSellerAndAddReputationToBoth() {
        var buyer = CryptoUserBuilder.defaultCryptoUser();
        var seller = CryptoUserBuilder.defaultCryptoUser();
        var transaction = Mockito.mock(Transaction.class);
        var intention = Mockito.mock(TradingIntention.class);

        Mockito.when(transaction.getBuyer()).thenReturn(buyer);
        Mockito.when(transaction.getSeller()).thenReturn(seller);
        Mockito.when(transaction.getTradingIntention()).thenReturn(intention);
        Mockito.when(intention.getCreatedAt()).thenReturn(Instant.MIN);

        seller.doTransaction(transaction);

        Assertions.assertEquals(1, buyer.getBuyTransactions().size());
        Assertions.assertEquals(1, seller.getSellTransactions().size());
        Assertions.assertEquals(5,buyer.getReputation());
        Assertions.assertEquals(5,seller.getReputation());
    }

    @Test
    public void whenDoCancelPenaltyUserShouldHave20lessReputation() {
        var user = CryptoUserBuilder.defaultCryptoUser();
        user.doCancelPenalty();
        Assertions.assertEquals(-20, user.getReputation());
    }

}
