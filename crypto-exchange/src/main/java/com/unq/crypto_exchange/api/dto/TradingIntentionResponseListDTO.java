package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TradingIntentionResponseListDTO {

    private CryptoCurrencyType cryptoCurrency;
    private String user;
    private OperationTypeDTO operation;
    private BigDecimal amount;
    private String status;
    private Instant createdAt;
    private Long quantity;
    private BigDecimal total;
    private Long operations;
    private String reputation;

    public static TradingIntentionResponseListDTO fromModel(TradingIntention tradingIntention) {
        return TradingIntentionResponseListDTO.builder()
                .cryptoCurrency(tradingIntention.getCryptoCurrencyType())
                .user(tradingIntention.getUser().getLastName() + ", " + tradingIntention.getUser().getName())
                .operation(OperationTypeDTO.fromModel(tradingIntention.getOperationType()))
                .amount(tradingIntention.getAmount())
                .status(tradingIntention.getStatus().name())
                .createdAt(tradingIntention.getCreatedAt())
                .quantity(tradingIntention.getQuantity())
                .total(tradingIntention.getAmount().multiply(BigDecimal.valueOf(tradingIntention.getQuantity())))
                .operations(tradingIntention.getUser().getNumberOperations())
                .reputation(getReputation(tradingIntention.getUser().getNumberOperations(), tradingIntention.getUser().getPoints()))
                .build();
    }

    public static List<TradingIntentionResponseListDTO> fromModel(List<TradingIntention> tradingIntentions) {
        return tradingIntentions.stream().map(TradingIntentionResponseListDTO::fromModel).toList();
    }

    private static String getReputation(Long numberOperations, Integer reputation) {

        if (numberOperations != 0) {
            var reputationValue = BigDecimal.valueOf(reputation);
            var operationsValue = BigDecimal.valueOf(numberOperations);
            var result = reputationValue.divide(operationsValue, 2, RoundingMode.HALF_UP);
            return result.toString();
        }
        return "No operations";
    }
}
