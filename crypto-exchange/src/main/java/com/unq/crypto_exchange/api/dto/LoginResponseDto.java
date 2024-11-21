package com.unq.crypto_exchange.api.dto;

public record LoginResponseDto(String token, long expiresIn) {
}
