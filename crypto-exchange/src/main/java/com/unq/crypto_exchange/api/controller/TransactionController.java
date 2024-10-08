package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.TransactionAction;
import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
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
    public TransactionResponseDTO requestP2P(@PathVariable("intentionId") Long intentionId, @PathVariable("userId") Long userId) {
        return TransactionResponseDTO.fromModel(transactionService.createTransaction(intentionId, userId));
    }

    @PostMapping("/confirm/{transactionId}")
    public TransactionResponseDTO completeTransaction(@PathVariable("transactionId") Long intentionId) {
        return TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CONFIRM));
    }

    @PostMapping("/cancel/{transactionId}")
    public TransactionResponseDTO cancelTransaction(@PathVariable("transactionId") Long intentionId) {
        return TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CANCEL));
    }
}
