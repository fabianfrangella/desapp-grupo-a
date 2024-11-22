package com.unq.crypto_exchange.external.dto;

import lombok.Data;

@Data
public class BinancePriceResponse {
    private String symbol;
    private String price;
}