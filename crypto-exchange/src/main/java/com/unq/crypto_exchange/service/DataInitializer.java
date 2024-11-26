package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.CryptoPriceRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@AllArgsConstructor
public class DataInitializer {

    private final UserService userService;
    private final CryptoPriceRepository cryptoPriceRepository;

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

        cryptoPriceRepository.saveAll(Arrays.stream(CryptoCurrencyType.values()).map(value -> CryptoPrice.builder()
                .price(BigDecimal.TEN)
                .time(LocalDateTime.now())
                .cryptoCurrencyType(value)
                .build()).toList());
    }
}
