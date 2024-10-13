package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPriceDTO {
    private CryptoCurrencyType cryptoCurrencyType;
    private LocalDateTime time;
    private BigDecimal price;

    public static CryptoPriceDTO fromModel(CryptoPrice cryptoPrice) {
        return CryptoPriceDTO.builder()
                .price(cryptoPrice.getPrice())
                .cryptoCurrencyType(cryptoPrice.getCryptoCurrencyType())
                .time(cryptoPrice.getTime())
                .build();
    }

    public static List<CryptoPriceDTO> fromModel(List<CryptoPrice> cryptoPrices) {
        return cryptoPrices.stream().map(CryptoPriceDTO::fromModel).toList();
    }
}
