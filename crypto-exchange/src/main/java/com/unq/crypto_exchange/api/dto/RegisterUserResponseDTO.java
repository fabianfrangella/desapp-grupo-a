package com.unq.crypto_exchange.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterUserResponseDTO {
    Long id;
    String name;
    String lastName;
    String email;
    String cvu;
    String cryptoWalletAddress;

    public static RegisterUserResponseDTO fromModel(CryptoUser user) {
        return RegisterUserResponseDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .cvu(user.getCvu())
                .cryptoWalletAddress(user.getCryptoWalletAddress())
                .build();
    }
}
