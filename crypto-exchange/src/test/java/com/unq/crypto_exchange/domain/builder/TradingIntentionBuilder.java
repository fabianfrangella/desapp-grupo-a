package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.*;

import java.math.BigDecimal;

public class TradingIntentionBuilder {

    public static TradingIntention withInactiveStatus() {
        return TradingIntention.builder().status(TradingIntention.Status.INACTIVE).build();
    }

    public static TradingIntention withDefaultData() {
        var intention = TradingIntention.builder()
                .amount(BigDecimal.ONE)
                .quantity(10L)
                .operationType(OperationType.PURCHASE)
                .cryptoCurrencyType(CryptoCurrencyType.AAVEUSDT)
                .build();
        intention.setId(1L);
        return intention;
    }

    public static TradingIntention withId(Long id) {
        var intention = TradingIntention.builder()
                .amount(BigDecimal.ONE)
                .quantity(10L)
                .operationType(OperationType.PURCHASE)
                .cryptoCurrencyType(CryptoCurrencyType.AAVEUSDT)
                .build();
        intention.setId(id);
        return intention;
    }

    public static TradingIntention withUserAndOperation(CryptoUser user, OperationType operation) {
        return TradingIntention.builder()
                .user(user)
                .amount(BigDecimal.ONE)
                .quantity(1L)
                .price(CryptoPrice.builder().build())
                .cryptoCurrencyType(CryptoCurrencyType.AAVEUSDT)
                .status(TradingIntention.Status.ACTIVE)
                .operationType(operation).build();
    }
}
