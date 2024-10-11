package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.domain.entity.transaction.TransactionAction;
import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
import com.unq.crypto_exchange.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

    @Operation(summary = "Creates a end-to-end of the transaction")
    @PostMapping("/intention/{intentionId}/user/{userId}")
    public TransactionResponseDTO requestP2P(
            @Parameter(description = "The trading intention id that will be processed", required = true) @PathVariable("intentionId") Long intentionId,
            @Parameter(description = "The user id who wants to operate", required = true) @PathVariable("userId") Long userId) {
        return TransactionResponseDTO.fromModel(transactionService.createTransaction(intentionId, userId));
    }

    @Operation(summary = "Confirms the transaction")
    @PostMapping("/confirm/{transactionId}")
    public TransactionResponseDTO completeTransaction(
            @Parameter(description = "The transaction id that will be completed", required = true) @PathVariable("transactionId") Long intentionId) {
        return TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CONFIRM));
    }

    @Operation(summary = "Cancels the transaction")
    @PostMapping("/cancel/{transactionId}")
    public TransactionResponseDTO cancelTransaction(
            @Parameter(description = "The transaction id that will be canceled", required = true) @PathVariable("transactionId") Long intentionId) {
        return TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CANCEL));
    }
}
