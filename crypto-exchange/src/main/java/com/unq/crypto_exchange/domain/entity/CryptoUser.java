package com.unq.crypto_exchange.domain.entity;


import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.domain.entity.transaction.strategy.BuyerUserStrategy;
import com.unq.crypto_exchange.domain.entity.transaction.strategy.SellerUserStrategy;
import com.unq.crypto_exchange.domain.entity.transaction.strategy.TransactionStrategy;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CryptoUser extends EntityMetaData {
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
    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> buyTransactions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Transaction> sellTransactions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TradingIntention> intentions = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CryptoActive> cryptoActives = new HashSet<>();

    @Transient
    private TransactionStrategy transactionStrategy;

    public TradingIntention makeIntention(TradingIntention intention, CryptoPrice cryptoPrice) {
        intention.setUser(this);
        intention.setStatus(TradingIntention.Status.ACTIVE);
        intention.setPrice(cryptoPrice);
        intentions.add(intention);
        return intention;
    }

    public TradingIntention cancelIntention(Long intentionId) {
        var intention = intentions.stream()
                .filter(i -> i.getId().equals(intentionId))
                .findFirst().orElseThrow(() -> new NoSuchTradingIntentionException("There is no TradingIntention with ID: " + intentionId));
        intention.setStatus(TradingIntention.Status.INACTIVE);
        return intention;
    }

    public void addBuyTransaction(Transaction transaction) {
        buyTransactions.add(transaction);
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent((crypto) -> {
                crypto.addQuantity(transaction.getQuantity());
            });
        }
    }

    public void addSellTransaction(Transaction transaction) {
        sellTransactions.add(transaction);
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent((crypto) -> {
                crypto.removeQuantity(transaction.getQuantity());
            });
        }
    }

    public void doCancelPenalty() {
        reputation-= 20;
    }

    public void addReputation(Integer reputation) {
        this.reputation+= reputation;
    }

    public void fillInitialWallet() {
        Arrays.stream(CryptoCurrencyType.values()).forEach(type -> {
            var cryptoActive = new CryptoActive();
            cryptoActive.setUser(this);
            cryptoActive.setQuantity(0L);
            cryptoActive.setType(type);

            cryptoActives.add(cryptoActive);
        });
    }

}
