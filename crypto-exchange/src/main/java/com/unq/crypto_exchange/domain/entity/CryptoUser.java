package com.unq.crypto_exchange.domain.entity;


import com.unq.crypto_exchange.domain.entity.exception.IllegalOperationException;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchCryptoCurrencyException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CryptoUser extends EntityMetaData implements UserDetails {
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

    public void makeIntention(TradingIntention intention, CryptoPrice cryptoPrice) {

        if (!hasEnoughQuantity(intention) && intention.getOperationType() == OperationType.SALE) {
            throw new IllegalOperationException("User does not have enough quantity to sell");
        }

        intention.setUser(this);
        intention.setStatus(TradingIntention.Status.ACTIVE);
        intention.setPrice(cryptoPrice);
        intentions.add(intention);
    }

    public boolean hasEnoughQuantity(TradingIntention intention){
        var cryptoActive = cryptoActives.stream()
                .filter(c -> c.getType().equals(intention.getCryptoCurrencyType()))
                .findFirst().orElseThrow(() -> new NoSuchCryptoCurrencyException("User does not have this type: " + intention.getCryptoCurrencyType()));

        return cryptoActive.getQuantity() >= intention.getQuantity();
    }

    public void addQuantity(Transaction transaction) {
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent(crypto -> crypto.addQuantity(transaction.getQuantity()));
        }
    }

    public void removeQuantity(Transaction transaction) {
        if (cryptoActives.stream().anyMatch(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency()))) {
            var maybeCrypto = cryptoActives.stream().filter(cryptoActive -> cryptoActive.getType().equals(transaction.getCryptoCurrency())).findFirst();
            maybeCrypto.ifPresent(crypto -> crypto.removeQuantity(transaction.getQuantity()));
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
            cryptoActive.setQuantity(1L);
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

    public List<CryptoActive> findCryptoActivesOperatedBetween(LocalDate from, LocalDate to) {
        return cryptoActives.stream()
                .filter(crypto ->
                    hasBeenOperatedBetween(from, to, crypto, sellTransactions ) ||
                    hasBeenOperatedBetween(from, to, crypto, buyTransactions)
                ).toList();
    }

    private boolean hasBeenOperatedBetween(LocalDate from, LocalDate to, CryptoActive crypto, Set<Transaction> transactions) {
        return transactions.stream()
                .anyMatch(transaction -> transaction.getCryptoCurrency() == crypto.getType()
                        && isBetween(from, to, LocalDate.ofInstant(transaction.getCreatedAt(), ZoneId.systemDefault())));
    }

    private boolean isBetween(LocalDate from, LocalDate to, LocalDate date) {
        return date.isEqual(from) ||  date.isEqual(to)|| (date.isAfter(from) && date.isBefore(to));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
