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
public class RegisterUserDTO {
    @NotNull
    @Size(min = 3, max = 30, message = "Name must be at least 3 characters long and maximum 30")
    String name;
    @NotNull
    @Size(min = 3, max = 30, message = "Last Name must be at least 3 characters long and maximum 30")
    String lastName;
    @NotNull
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Invalid email address!")
    String email;
    @NotNull
    @Pattern(regexp = "(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(=?.*[\\p{P}\\p{S}]).*", message = "Password must have at least 1 uppercase, 1 lowercase and 1 special character")
    @Size(min = 6, message = "Password length must be at least 6 characters")
    String password;
    @NotNull
    @Size(min = 22, max = 22, message = "CVU must be 22 characters long")
    String cvu;
    @NotNull
    @Size(min = 8, max = 8, message = "Crypto Wallet Address must be 8 characters long")
    String cryptoWalletAddress;

    public static RegisterUserDTO fromModel(CryptoUser user) {
        return RegisterUserDTO
                .builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .cvu(user.getCvu())
                .cryptoWalletAddress(user.getCryptoWalletAddress())
                .build();
    }

    public CryptoUser toModel() {
        return CryptoUser.builder()
                .name(name)
                .lastName(lastName)
                .password(password)
                .email(email)
                .cvu(cvu)
                .cryptoWalletAddress(cryptoWalletAddress)
                .build();
    }

}
