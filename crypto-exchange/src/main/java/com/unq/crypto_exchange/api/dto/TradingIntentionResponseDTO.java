package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.OperationType;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    //TODO: Ver de refactorizar y heredar de ResponseList para reutilizar
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
                .reputation(getReputation(tradingIntention.getUser().getNumberOperations(), tradingIntention.getUser().getReputation()))
                .accountAddress(getAccountAddress(tradingIntention.getOperationType(), tradingIntention.getUser()))
                .confirmAction(tradingIntention.getOperationType() == OperationType.SALE ? "Confirm recepit" : "I made the transfer")
                .cancelAction("Cancel")
                .operation(OperationTypeDTO.fromModel(tradingIntention.getOperationType()))
                .build();
    }

    private static String getAccountAddress(OperationType operationType, CryptoUser user) {
        if (operationType == OperationType.SALE) {
            return user.getCvu();
        }
        return user.getCryptoWalletAddress();
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
