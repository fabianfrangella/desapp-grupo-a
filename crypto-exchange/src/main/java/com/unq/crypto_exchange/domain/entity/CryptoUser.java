package com.unq.crypto_exchange.domain.entity;


import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
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

    @Transient
    private TransactionStrategy transactionStrategy;

    public TradingIntention makeIntention(TradingIntention intention) {
        intention.setUser(this);
        intention.setStatus(TradingIntention.Status.ACTIVE);
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

    public Transaction doTransaction(Transaction transaction) {
        if (transaction.getBuyer().equals(this)) {
            transactionStrategy = new BuyerUserStrategy();
        } else {
            transactionStrategy = new SellerUserStrategy();
        }
        transactionStrategy.doTransaction(this, transaction);
        return transaction;
    }

    public void addBuyTransaction(Transaction transaction) {
        buyTransactions.add(transaction);
    }

    public void addSellTransaction(Transaction transaction) {
        sellTransactions.add(transaction);
    }

    public void doCancelPenalty() {
        reputation-= 20;
    }

    public void addReputation(Integer reputation) {
        this.reputation+= reputation;
    }

}
