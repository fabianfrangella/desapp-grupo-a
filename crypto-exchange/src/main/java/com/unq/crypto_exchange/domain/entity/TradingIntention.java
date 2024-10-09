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
        if (user.getId().equals(requestUser.getId())) throw new IllegalOperationException("Buyer and Seller user must be different");

        var transactionStatus = Transaction.TransactionStatus.PENDING;

        if (isAmountOutOfFivePercentRange()) {
            status = Status.INACTIVE;
            transactionStatus = Transaction.TransactionStatus.CANCELED;
        }

        var buyerUser = operationType == OperationType.PURCHASE ? user : requestUser;
        var sellerUser = operationType == OperationType.SALE ? user : requestUser;

        return Transaction.builder()
                .tradingIntention(this)
                .amount(amount)
                .price(price)
                .buyer(buyerUser)
                .seller(sellerUser)
                .operationType(operationType)
                .quantity(quantity)
                .cryptoCurrency(cryptoCurrencyType)
                .status(transactionStatus)
                .build();
    }

    private boolean isAmountOutOfFivePercentRange() {
        BigDecimal fivePercent = price.getPrice().multiply(new BigDecimal("0.05"));

        BigDecimal lowerLimit = price.getPrice().subtract(fivePercent);
        BigDecimal upperLimit = price.getPrice().add(fivePercent);

        return amount.compareTo(lowerLimit) < 0 || amount.compareTo(upperLimit) > 0;
    }
}
