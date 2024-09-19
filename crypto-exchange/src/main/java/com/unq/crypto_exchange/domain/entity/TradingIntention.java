package com.unq.crypto_exchange.domain.entity;

import com.unq.crypto_exchange.domain.entity.exception.IllegalCancelOperationException;
import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.InactiveTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingIntention extends EntityMetaData {

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

    public Transaction doTransaction(CryptoUser requestUser) {
        if (status == Status.INACTIVE) throw new InactiveTradingIntentionException("The TradingIntention is Inactive");
        if (operationType == OperationType.CANCEL && !requestUser.equals(user)) {
            throw new IllegalCancelOperationException("Only the System or the owner of the TradingIntention can cancel it");
        }
        if (operationType == OperationType.CANCEL) {
            user.doCancelPenalty();
            return null;
        }
        if (user.getId().equals(requestUser.getId())) throw new IllegalOperationException("Buyer and Seller user must be different");
        if (operationType == OperationType.SYSTEM_CANCEL) {
            return null;
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
                .status(Transaction.TransactionStatus.PENDING)
                .build();

        status = Status.INACTIVE;
        return requestUser.doTransaction(transaction);
    }
}
