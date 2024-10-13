package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TradingIntentionDTO {

    private CryptoCurrencyType cryptoCurrency;
    private Long quantity;
    private BigDecimal amount;
    private OperationTypeDTO operationType;

    public TradingIntention toModel() {
        return TradingIntention.builder()
                .amount(amount)
                .operationType(operationType.toModel())
                .quantity(quantity)
                .cryptoCurrencyType(cryptoCurrency)
                .build();
    }
}
