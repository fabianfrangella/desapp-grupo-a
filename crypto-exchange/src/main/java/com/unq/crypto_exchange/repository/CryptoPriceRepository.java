package com.unq.crypto_exchange.repository;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {

    Optional<CryptoPrice> findFirstByCryptoCurrencyTypeOrderByTimeDesc(CryptoCurrencyType cryptoCurrencyType);

    @Query("""
            FROM CryptoPrice cp
            WHERE cp.time = (SELECT max(time) from CryptoPrice WHERE cryptoCurrencyType = cp.cryptoCurrencyType)
            ORDER BY cp.time DESC
            """)
    List<CryptoPrice> findLatestCryptoPrices();
}
