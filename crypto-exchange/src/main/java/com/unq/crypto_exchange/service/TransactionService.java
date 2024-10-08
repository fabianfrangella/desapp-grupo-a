package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.transaction.TransactionAction;
import com.unq.crypto_exchange.domain.entity.exception.NoSuchTradingIntentionException;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.repository.TradingIntentionRepository;
import com.unq.crypto_exchange.repository.TransactionRepository;
import com.unq.crypto_exchange.repository.UserRepository;
import com.unq.crypto_exchange.service.exception.UserNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TransactionService {

    private final UserRepository userRepository;
    private final TradingIntentionRepository tradingIntentionRepository;
    private final TransactionRepository transactionRepository;
    public Transaction createTransaction(Long tradingIntentionId, Long userId) {
        var intention = tradingIntentionRepository.findById(tradingIntentionId)
                .orElseThrow(() -> new NoSuchTradingIntentionException("TradingIntention with id: " + tradingIntentionId + " was not found"));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id: " + userId + " was not found"));

        var transaction = intention.doTransaction(user);

        transactionRepository.save(transaction);

        return transaction;
    }

    public Transaction processTransaction(Long transactionId, TransactionAction action) {
        var transaction = transactionRepository.findById(transactionId).orElseThrow(() -> new RuntimeException("Transaction not found"));
        transaction.process(action);
        return transaction;
    }
}
