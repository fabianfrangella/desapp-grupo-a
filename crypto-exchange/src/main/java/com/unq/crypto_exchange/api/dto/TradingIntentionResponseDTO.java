package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Data
@Builder
public class TradingIntentionResponseDTO {
    private CryptoCurrencyType cryptoCurrency;
    private Long quantity;
    private CryptoPriceDTO cryptoPrice;
    private BigDecimal amount;
    private String user;
    private Long operations;
    private String reputation;
    private String accountAddress;
    private String confirmAction;
    private String cancelAction;
    private OperationTypeDTO operation;
    private Long id;

    public static TradingIntentionResponseDTO fromModel(TradingIntention tradingIntention) {
        return TradingIntentionResponseDTO.builder()
                .cryptoCurrency(tradingIntention.getCryptoCurrencyType())
                .quantity(tradingIntention.getQuantity())
                .cryptoPrice(CryptoPriceDTO.builder()
                        .cryptoCurrencyType(tradingIntention.getPrice().getCryptoCurrencyType())
                        .price(tradingIntention.getPrice().getPrice())
                        .time(tradingIntention.getPrice().getTime())
                        .build())
                .amount(tradingIntention.getAmount())
                .user(tradingIntention.getUser().getLastName() + ", " + tradingIntention.getUser().getName())
                .operations(tradingIntention.getUser().getNumberOperations())
                .reputation(!Objects.equals(tradingIntention.getUser().getReputation(), BigDecimal.ZERO) ?
                        tradingIntention.getUser().getReputation().toString() : "No operations")
                .accountAddress(tradingIntention.getOperationType() == OperationType.SALE ?
                        tradingIntention.getUser().getCvu() : tradingIntention.getUser().getCryptoWalletAddress())
                .confirmAction(tradingIntention.getOperationType() == OperationType.SALE ? "Confirm recepit" : "I made the transfer")
                .cancelAction("Cancel")
                .operation(OperationTypeDTO.fromModel(tradingIntention.getOperationType()))
                .id(tradingIntention.getId())
                .build();
    }
}
