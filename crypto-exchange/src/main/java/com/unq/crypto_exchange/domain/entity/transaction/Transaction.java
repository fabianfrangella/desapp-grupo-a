package com.unq.crypto_exchange.domain.entity.transaction;

import com.unq.crypto_exchange.domain.entity.*;
import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "trading_intention_id")
    private TradingIntention tradingIntention;

    private TransactionStatus status = TransactionStatus.PENDING;

    public TransactionStatus process(TransactionAction action) {
        // TODO: revisar esto
        if (getStatus() == Transaction.TransactionStatus.COMPLETED || getStatus() == Transaction.TransactionStatus.CANCELED) {
            tradingIntention.setStatus(TradingIntention.Status.INACTIVE);
            throw new IllegalOperationException("The transaction has been already processed");
        }

        if (tradingIntention.getStatus() == TradingIntention.Status.INACTIVE) {
            tradingIntention.setStatus(TradingIntention.Status.INACTIVE);
            throw new InactiveTradingIntentionException("Trading intention is inactive");
        }

        if (!seller.hasEnoughQuantity(tradingIntention)) {
            tradingIntention.setStatus(TradingIntention.Status.INACTIVE);
            return status;
        }

        if (action == TransactionAction.CANCEL) {
            cancel();
            tradingIntention.getUser().doCancelPenalty();
            return status;
        }
        var points = 5;
        if (Instant.now().isBefore(tradingIntention.getCreatedAt().plus(30, ChronoUnit.MINUTES))) {
            points = 10;
        }
        confirm();
        buyer.addPoints(points);
        seller.addPoints(points);
        buyer.addQuantity(this);
        seller.removeQuantity(this);
        tradingIntention.setStatus(TradingIntention.Status.INACTIVE);
        return status;
    }

    public void confirm() {
        this.status = TransactionStatus.COMPLETED;
    }

    public void cancel() {
        this.status = TransactionStatus.CANCELED;
    }

    public void fail() {
        this.status = TransactionStatus.FAILED;
    }

    public enum TransactionStatus {
        COMPLETED,
        PENDING,
        CANCELED,
        FAILED
    }
}
