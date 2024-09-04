package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoUser;

public record RegisterUserDTO(String name,
                              String lastName,
                              String email,
                              String password,
                              String cvu,
                              String cryptoWalletAddress) {
    public CryptoUser toModel() {
        return CryptoUser.builder()
                .name(name)
                .lastName(lastName)
                .password(password)
                .email(email)
                .cvu(cvu)
                .cryptoWalletAddress(cryptoWalletAddress)
                .build();
    }

}
