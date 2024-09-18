package com.unq.crypto_exchange.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction extends EntityMetaData {
    @NonNull
    private CryptoCurrencyType cryptoCurrency;
    @NonNull
    private Long quantity;
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private CryptoPrice price;
    @NonNull
    private BigDecimal amount;
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CryptoUser user;
    @NonNull
    private OperationType operationType;
}
