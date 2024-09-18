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
    private String userFirstName;
    private String userLastName;
    private Integer userOperationsAmount;
    private Integer reputation;
    private String accountAddress;
    private String action;

    public static TransactionResponseDTO fromModel(Transaction transaction) {
        return TransactionResponseDTO.builder()
                .cryptoCurrency(transaction.getCryptoCurrency())
                .quantity(transaction.getQuantity())
                .cryptoPrice(CryptoPriceDTO.builder()
                        .cryptoCurrencyType(transaction.getCryptoCurrency())
                        .price(transaction.getPrice().getPrice())
                        .time(transaction.getPrice().getTime())
                        .build())
                .userFirstName("")
                .userLastName("")
                .reputation(0)
                .accountAddress("")
                .action("")
                .build();
    }
}
