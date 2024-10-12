package com.unq.crypto_exchange.domain.entity;


import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchCryptoCurrencyException;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

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
    private Integer points = 0;

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

    public TradingIntention makeIntention(TradingIntention intention, CryptoPrice cryptoPrice) {

        if (!hasEnoughQuantity(intention) && intention.getOperationType() == OperationType.SALE) {
            throw new IllegalOperationException("User does not have enough quantity to sell");
        }

        intention.setUser(this);
        intention.setStatus(TradingIntention.Status.ACTIVE);
        intention.setPrice(cryptoPrice);
        intentions.add(intention);
        return intention;
    }

    public boolean hasEnoughQuantity(TradingIntention intention){
        var cryptoActive = cryptoActives.stream()
                .filter(c -> c.getType().equals(intention.getCryptoCurrencyType()))
                .findFirst().orElseThrow(() -> new NoSuchCryptoCurrencyException("User does not have this type: " + intention.getCryptoCurrencyType()));

        return cryptoActive.getQuantity() >= intention.getQuantity();
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
        addQuantity(transaction);
    }

    public void addSellTransaction(Transaction transaction) {
        sellTransactions.add(transaction);
        removeQuantity(transaction);
    }

    public void addQuantity(Transaction transaction) {
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent((crypto) -> {
                crypto.addQuantity(transaction.getQuantity());
            });
        }
    }

    public void removeQuantity(Transaction transaction) {
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent((crypto) -> {
                crypto.removeQuantity(transaction.getQuantity());
            });
        }
    }

    public void doCancelPenalty() {
        points = Math.max(0, points - 20);
    }

    public void addPoints(Integer points) {
        this.points += points;
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

    public BigDecimal getReputation() {
        var numberOperations = getNumberOperations();

        if (numberOperations != 0) {
            var reputationValue = BigDecimal.valueOf(points);
            var operationsValue = BigDecimal.valueOf(numberOperations);
            return reputationValue.divide(operationsValue, 2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    public Long getNumberOperations() {
        return getSizeCompletedOperations(buyTransactions) +
                getSizeCompletedOperations(sellTransactions);
    }

    private Long getSizeCompletedOperations (Set<Transaction> transactions) {
        return (long) transactions.stream()
                .filter((transaction -> transaction.getStatus() == Transaction.TransactionStatus.COMPLETED))
                .toList()
                .size();
    }

    public List<CryptoActive> findCryptoActivesOperatedBetween(Date from, Date to) {
        return cryptoActives.stream()
                .filter(crypto ->
                    hasBeenOperatedBetween(from, to, crypto, sellTransactions ) ||
                    hasBeenOperatedBetween(from, to, crypto, buyTransactions)
                ).collect(Collectors.toList());
    }

    private boolean hasBeenOperatedBetween(Date from, Date to, CryptoActive crypto, Set<Transaction> transactions) {
        return transactions.stream()
                .anyMatch(transaction -> transaction.getCryptoCurrency() == crypto.getType()
                        && isBetween(from, to, Date.from(transaction.getCreatedAt())));
    }

    private boolean isBetween(Date from, Date to, Date date) {
        return date.after(from) && date.before(to);
    }
}
