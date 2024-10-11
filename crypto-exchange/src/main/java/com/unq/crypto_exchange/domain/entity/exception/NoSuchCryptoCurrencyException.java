package com.unq.crypto_exchange.domain.entity.exception;

public class NoSuchCyptoCurrencyException extends RuntimeException {
    public NoSuchCyptoCurrencyException(String message) {
        super(message);
    }
}