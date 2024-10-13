package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
    private Long id;

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
                .reputation(!Objects.equals(tradingIntention.getUser().getReputation(), BigDecimal.ZERO) ?
                        tradingIntention.getUser().getReputation().toString() : "No operations")
                .id(tradingIntention.getId())
                .build();
    }

    public static List<TradingIntentionResponseListDTO> fromModel(List<TradingIntention> tradingIntentions) {
        return tradingIntentions.stream().map(TradingIntentionResponseListDTO::fromModel).toList();
    }
}
