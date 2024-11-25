package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer {

    private final UserService userService;
    private final CryptoPriceService cryptoPriceService;

    @PostConstruct
    public void initialize() {
        userService.register(CryptoUser.builder()
                        .cryptoWalletAddress("12345678")
                        .email("fabian@test.com")
                        .password("1234568#aA")
                        .name("Fabian")
                        .lastName("Frangella")
                        .cvu("1234567891123456789112")
                .build());

        userService.register(CryptoUser.builder()
                .cryptoWalletAddress("12345679")
                .email("andres@test.com")
                .password("1234568#aA")
                .name("Andres")
                .lastName("Mora")
                .cvu("1234567891123456789113")
                .build());
        cryptoPriceService.findLast();
    }
}
