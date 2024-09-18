package com.unq.crypto_exchange.repository;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {

    CryptoPrice findFirstByCryptoCurrencyTypeOrderByTimeDesc(CryptoCurrencyType cryptoCurrencyType);
}
