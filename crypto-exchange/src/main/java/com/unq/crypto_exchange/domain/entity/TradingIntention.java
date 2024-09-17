package com.unq.crypto_exchange.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingIntention extends PersistentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private CryptoCurrencyType cryptoCurrencyType;
    private Long quantity;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "price_id")
    private CryptoPrice price;
    private BigDecimal amount;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private CryptoUser user;
    private OperationType operationType;
    private Status status;

    enum Status {
        ACTIVE, INACTIVE
    }
}
