package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponseDTO {

    private CryptoCurrencyType cryptoCurrency;
    private Long quantity;
    private CryptoPriceDTO cryptoPrice;
    private String buyer;
    private String seller;
    private TransactionStatus status;

    public static TransactionResponseDTO fromModel(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .cryptoCurrency(transaction.getCryptoCurrency())
                .quantity(transaction.getQuantity())
                .cryptoPrice(CryptoPriceDTO.builder()
                        .cryptoCurrencyType(transaction.getCryptoCurrency())
                        .price(transaction.getPrice().getPrice())
                        .time(transaction.getPrice().getTime())
                        .build())
                .buyer(transaction.getBuyer().getLastName() + ", " + transaction.getBuyer().getName())
                .seller(transaction.getSeller().getLastName() + ", " + transaction.getSeller().getName())
                .status(TransactionStatus.fromModel(transaction.getStatus()))
                .build();
    }

    public enum TransactionStatus {
        COMPLETED,
        PENDING,
        CANCELED,
        FAILED;

        public static TransactionStatus fromModel(Transaction.TransactionStatus status) {
            return switch(status) {
                case Transaction.TransactionStatus.CANCELED -> CANCELED;
                case Transaction.TransactionStatus.COMPLETED -> COMPLETED;
                case Transaction.TransactionStatus.PENDING -> PENDING;
                case Transaction.TransactionStatus.FAILED -> FAILED;
            };
        }
    }
}
