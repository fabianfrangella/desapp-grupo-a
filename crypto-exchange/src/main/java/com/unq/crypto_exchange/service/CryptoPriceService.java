package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.repository.CryptoPriceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class CryptoPriceService {
    private final CryptoPriceRepository cryptoPriceRepository;

    public List<CryptoPrice> find() {
        return cryptoPriceRepository.findAll();
    }
}
