package com.unq.crypto_exchange.domain.entity.exception;

public class NoSuchCryptoCurrencyException extends RuntimeException {
    public NoSuchCryptoCurrencyException(String message) {
        super(message);
    }
}