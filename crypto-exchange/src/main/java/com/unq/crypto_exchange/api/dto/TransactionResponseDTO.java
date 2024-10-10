package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionResponseDTO {

    private CryptoCurrencyType cryptoCurrency;
    private Long quantity;
    private CryptoPriceDTO cryptoPrice;
    private String buyer;
    private String seller;
    private String status;

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
                .status(transaction.getStatus().name())
                .build();
    }
}
