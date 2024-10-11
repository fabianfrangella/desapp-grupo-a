package com.unq.crypto_exchange.domain.builder;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.domain.entity.CryptoActive;
import java.util.HashSet;
import java.util.Set;

public class CryptoUserBuilder {
    private String name = "Test name";
    private String lastName = "Test last name";
    private String email = "email@test.com";
    private String password = "Password1#a";
    private String cvu = "12345678901234567890";
    private String cryptoWalletAddress = "12345678";
    private Integer points = 0;
    private Set<Transaction> buyTransactions = new HashSet<>();
    private Set<Transaction> sellTransactions = new HashSet<>();
    private Set<TradingIntention> intentions = new HashSet<>();
    private Set<CryptoActive> cryptoActives = new HashSet<>();

    public static CryptoUserBuilder aCryptoUser() {
        return new CryptoUserBuilder();
    }

    public CryptoUser build() {
        CryptoUser cryptoUser = new CryptoUser();
        cryptoUser.setName(name);
        cryptoUser.setLastName(lastName);
        cryptoUser.setEmail(email);
        cryptoUser.setPassword(password);
        cryptoUser.setCvu(cvu);
        cryptoUser.setCryptoWalletAddress(cryptoWalletAddress);
        cryptoUser.setPoints(points);
        cryptoUser.setBuyTransactions(buyTransactions);
        cryptoUser.setSellTransactions(sellTransactions);
        cryptoUser.setIntentions(intentions);
        cryptoUser.setCryptoActives(cryptoActives);
        return cryptoUser;
    }

    public CryptoUserBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CryptoUserBuilder withLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public CryptoUserBuilder withEmail(final String email) {
        this.email = email;
        return this;
    }

    public CryptoUserBuilder withPassword(final String password) {
        this.password = password;
        return this;
    }

    public CryptoUserBuilder withCvu(final String cvu) {
        this.cvu = cvu;
        return this;
    }

    public CryptoUserBuilder withCryptoWalletAddress(final String cryptoWalletAddress) {
        this.cryptoWalletAddress = cryptoWalletAddress;
        return this;
    }

    public CryptoUserBuilder withPoints(final Integer points) {
        this.points = points;
        return this;
    }

    public CryptoUserBuilder withBuyTransactions(final Set<Transaction> buyTransactions) {
        this.buyTransactions = buyTransactions;
        return this;
    }

    public CryptoUserBuilder withSellTransactions(final Set<Transaction> sellTransactions) {
        this.sellTransactions = sellTransactions;
        return this;
    }

    public CryptoUserBuilder withIntentions(final Set<TradingIntention> intentions) {
        this.intentions = intentions;
        return this;
    }

    public CryptoUserBuilder withCryptoActives(final Set<CryptoActive> cryptoActives) {
        this.cryptoActives = cryptoActives;
        return this;
    }
}
