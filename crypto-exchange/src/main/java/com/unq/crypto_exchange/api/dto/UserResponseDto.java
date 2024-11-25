package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record UserResponseDto(String name, String lastName, Long operations, BigDecimal reputation) {
    public static UserResponseDto fromModel(CryptoUser model) {
        return UserResponseDto.builder()
                .name(model.getName())
                .lastName(model.getLastName())
                .reputation(model.getReputation())
                .operations(model.getNumberOperations())
                .build();
    }
}
