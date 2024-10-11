package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoUser;

import java.util.Collections;

public class CryptoUserBuilder {

    public static CryptoUser defaultCryptoUser() {
        return CryptoUser.builder()
                .cvu("1111111111111111111111")
                .name("Test")
                .lastName("Test")
                .email("email@test.com")
                .password("Password1#a")
                .cryptoWalletAddress("12345678")
                .cryptoActives(Collections.singleton(CryptoActiveBuilder.defaultCryptoActive()))
                .build();
    }
}
