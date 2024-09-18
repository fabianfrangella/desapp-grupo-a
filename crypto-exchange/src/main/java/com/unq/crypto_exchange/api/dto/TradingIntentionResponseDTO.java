package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.*;

@Data
@Builder
public class TradingIntentionResponseDTO {

    private CryptoCurrencyType cryptoCurrency;
    private CryptoPriceDTO price;
    private String userFirstName;
    private String userLastName;
    private OperationTypeDTO operation;

    public static TradingIntentionResponseDTO fromModel(TradingIntention tradingIntention) {
        return TradingIntentionResponseDTO.builder()
                .cryptoCurrency(tradingIntention.getCryptoCurrencyType())
                .price(CryptoPriceDTO.builder()
                        .time(tradingIntention.getPrice().getTime())
                        .cryptoCurrencyType(tradingIntention.getCryptoCurrencyType())
                        .price(tradingIntention.getPrice().getPrice())
                        .build())
                .userFirstName(tradingIntention.getUser().getName())
                .userLastName(tradingIntention.getUser().getLastName())
                .operation(OperationTypeDTO.fromModel(tradingIntention.getOperationType()))
                .build();
    }
}
