package com.unq.crypto_exchange.external.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CryptoDolarResponse {
    private String moneda;
    private String casa;
    private String nombre;
    private BigDecimal compra;
    private BigDecimal venta;
    private LocalDateTime fechaActualizacion;
}
