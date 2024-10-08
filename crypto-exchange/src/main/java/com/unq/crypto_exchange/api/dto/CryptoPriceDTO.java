package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class CryptoPriceDTO {
    private CryptoCurrencyType cryptoCurrencyType;
    private LocalDateTime time;
    private BigDecimal price;
}
