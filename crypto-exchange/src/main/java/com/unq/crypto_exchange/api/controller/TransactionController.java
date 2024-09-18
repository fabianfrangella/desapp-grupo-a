package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
import com.unq.crypto_exchange.domain.entity.transaction.Transaction;
import com.unq.crypto_exchange.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/intention/{intentionId}/user/{userId}")
    public TransactionResponseDTO processTransaction(@PathVariable("intentionId") Long intentionId, @PathVariable("userId") Long userId) {
        return TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, userId));
    }
}
