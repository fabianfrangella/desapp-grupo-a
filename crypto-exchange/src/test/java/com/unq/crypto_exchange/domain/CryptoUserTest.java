package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoActiveBuilder;
import com.unq.crypto_exchange.domain.builder.CryptoUserBuilder;
import com.unq.crypto_exchange.domain.builder.TradingIntentionBuilder;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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

}
