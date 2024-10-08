package com.unq.crypto_exchange.domain.entity.exception;

import lombok.AllArgsConstructor;

public class NoSuchTradingIntentionException extends RuntimeException {
    public NoSuchTradingIntentionException(String message) {
        super(message);
    }
}
