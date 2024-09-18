package com.unq.crypto_exchange.service.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {
        super(String.format("User with email: %s already exists", email));
    }
}
