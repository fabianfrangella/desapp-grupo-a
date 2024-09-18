package com.unq.crypto_exchange.domain.entity.exception;

public class IllegalCancelOperationException extends RuntimeException {
    public IllegalCancelOperationException(String message) {
        super(message);
    }
}
