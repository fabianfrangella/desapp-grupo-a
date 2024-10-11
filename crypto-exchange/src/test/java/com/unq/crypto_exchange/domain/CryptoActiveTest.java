package com.unq.crypto_exchange.domain;

import com.unq.crypto_exchange.domain.builder.CryptoActiveBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CryptoActiveTest {

    @Test
    public void whenAddQuantityShouldWorks() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withQuantity(100L)
                .build();

        cryptoActive.addQuantity(10L);

        Assertions.assertEquals(110L, cryptoActive.getQuantity());
    }

    @Test
    public void whenRemoveQuantityShouldWorks() {
        var cryptoActive = CryptoActiveBuilder.aCryptoActive()
                .withQuantity(100L)
                .build();

        cryptoActive.removeQuantity(10L);

        Assertions.assertEquals(90L, cryptoActive.getQuantity());
    }
}
