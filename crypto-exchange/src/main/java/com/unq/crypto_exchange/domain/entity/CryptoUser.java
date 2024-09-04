package com.unq.crypto_exchange.domain.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CryptoUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Size(min = 3, max = 30)
    private String name;
    @NonNull
    @Size(min = 3, max = 30)
    private String lastName;
    @NonNull
    @Pattern(regexp=".+@.+\\.[a-z]+", message="Invalid email address!")
    private String email;
    @NonNull
    private String password;
    @NonNull
    @Size(min = 22, max = 22)
    private String cvu;
    @NonNull
    @Size(min = 8, max = 8)
    private String cryptoWalletAddress;
}
