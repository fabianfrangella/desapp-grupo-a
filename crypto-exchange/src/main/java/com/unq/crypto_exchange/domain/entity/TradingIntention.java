package com.unq.crypto_exchange.domain.entity;

import com.unq.crypto_exchange.domain.entity.exception.IllegalCancelOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingIntention extends EntityMetaData {

    private static final Logger logger = LoggerFactory.getLogger(TradingIntention.class);
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

    public enum Status {
        ACTIVE, INACTIVE
    }

    public void doTransaction(CryptoUser requestUser) {
        if (status == Status.INACTIVE) throw new InactiveTradingIntentionException("The TradingIntention is Inactive");
        if (operationType == OperationType.CANCEL && !requestUser.equals(user)) {
            throw new IllegalCancelOperationException("Only the System or the owner of the TradingIntention can cancel it");
        }
        if (operationType == OperationType.CANCEL) {
            user.doCancelPenalty();
            return;
        }
        if (operationType == OperationType.SYSTEM_CANCEL) {
            return;
        }

        var buyerUser = operationType == OperationType.PURCHASE ? user : requestUser;
        var sellerUser = operationType == OperationType.SALE ? user : requestUser;

        var transaction = Transaction.builder()
                .tradingIntention(this)
                .amount(amount)
                .price(price)
                .buyer(buyerUser)
                .seller(sellerUser)
                .operationType(operationType)
                .quantity(quantity)
                .cryptoCurrency(cryptoCurrencyType)
                .build();

        requestUser.doTransaction(transaction);
    }
}
