package com.unq.crypto_exchange.domain.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CryptoUser extends PersistentEntity {
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
    @Column(unique = true)
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

    @Builder.Default
    @NonNull
    private Integer reputation = 0;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> transactions = new HashSet<>();

}
