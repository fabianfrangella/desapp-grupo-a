package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoActive;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OperatedCryptoDTO(LocalDateTime dateTime,
                                BigDecimal totalUsdValue,
                                BigDecimal totalArsValue,
                                List<CryptoActiveDTO> cryptos) {

    public record CryptoActiveDTO(CryptoCurrencyType crypto, Long quantity, CryptoPriceDTO price, BigDecimal arsValue) {
        public static CryptoActiveDTO fromModel(CryptoActive cryptoActive, CryptoPrice price) {
            return new CryptoActiveDTO(cryptoActive.getType(), cryptoActive.getQuantity(), CryptoPriceDTO.fromModel(price), BigDecimal.ZERO);
        }
    }
}

/*
Dia y hora de solicitud
Valor total operado en dólares
Valor total operado en pesos ARG
Activos:
Criptoactivo
Cantidad nominal del Cripto Activo
Cotización actual del Cripto Activo
Monto de la cotización en pesos ARG

 */