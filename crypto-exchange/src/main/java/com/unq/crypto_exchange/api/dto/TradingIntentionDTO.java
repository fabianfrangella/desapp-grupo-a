package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
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
