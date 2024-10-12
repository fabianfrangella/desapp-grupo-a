package com.unq.crypto_exchange.domain.entity.exception;

public class NoSuchTradingIntentionException extends RuntimeException {
    public NoSuchTradingIntentionException(String message) {
        super(message);
    }
}
