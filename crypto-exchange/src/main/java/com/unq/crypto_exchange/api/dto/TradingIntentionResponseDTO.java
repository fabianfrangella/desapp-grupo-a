package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class TradingIntentionResponseDTO {

    private CryptoCurrencyType cryptoCurrency;
    private CryptoPriceDTO price;
    private String userFirstName;
    private String userLastName;
    private OperationTypeDTO operation;
    private BigDecimal amount;
    private String status;

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
                .amount(tradingIntention.getAmount())
                .status(tradingIntention.getStatus().name())
                .build();
    }

    public static List<TradingIntentionResponseDTO> fromModel(List<TradingIntention> tradingIntentions) {
        return tradingIntentions.stream().map(TradingIntentionResponseDTO::fromModel).toList();
    }
}
