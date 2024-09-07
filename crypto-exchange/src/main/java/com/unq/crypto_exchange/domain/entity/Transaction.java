package com.unq.crypto_exchange.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private CryptoCurrencyType cryptoCurrency;

    @NonNull
    private Integer quantity;

    @NonNull
    private Float price;

    @NonNull
    private Float amount;

    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CryptoUser user;

    @NonNull
    private OperationType operationType;
}
