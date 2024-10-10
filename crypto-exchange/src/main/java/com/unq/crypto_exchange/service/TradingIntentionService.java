package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.TradingIntention;
import com.unq.crypto_exchange.repository.CryptoPriceRepository;
import com.unq.crypto_exchange.repository.TradingIntentionRepository;
import com.unq.crypto_exchange.repository.UserRepository;
import com.unq.crypto_exchange.service.exception.PriceNotFoundException;
import com.unq.crypto_exchange.service.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class TradingIntentionService {

    private final UserRepository userRepository;
    private final CryptoPriceRepository cryptoPriceRepository;
    private final TradingIntentionRepository intentionRepository;

    public TradingIntention publishTradingIntention(Long userId, TradingIntention tradingIntention) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));;
        var price = cryptoPriceRepository.findFirstByCryptoCurrencyTypeOrderByTimeDesc(tradingIntention.getCryptoCurrencyType())
                .orElseThrow(() -> new PriceNotFoundException("Price not found for crypto"));
        user.makeIntention(tradingIntention, price);
        userRepository.save(user);
        return tradingIntention;
    }

    public List<TradingIntention> find() {
        return intentionRepository.findAll();
    }

}
