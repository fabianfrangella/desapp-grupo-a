package com.unq.crypto_exchange.domain.entity.transaction;

import com.unq.crypto_exchange.domain.entity.*;
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
    @JoinColumn(name = "seller_user_id")
    private CryptoUser seller;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_user_id")
    private CryptoUser buyer;
    @NonNull
    private OperationType operationType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trading_intention_id")
    private TradingIntention tradingIntention;

    private TransactionStatus status = TransactionStatus.PENDING;

    public void confirm() {
        this.status = TransactionStatus.COMPLETED;
    }

    public void cancel() {
        this.status = TransactionStatus.CANCELED;
    }

    public enum TransactionStatus {
        COMPLETED,
        PENDING,
        CANCELED
    }
}
