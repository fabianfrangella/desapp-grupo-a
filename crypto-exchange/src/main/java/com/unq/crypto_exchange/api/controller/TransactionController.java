package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.domain.entity.transaction.TransactionAction;
import com.unq.crypto_exchange.api.dto.TransactionResponseDTO;
import com.unq.crypto_exchange.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Transactional
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Creates a end-to-end of the transaction")
    @PostMapping("/intention/{intentionId}/user/{userId}")
    public ResponseEntity<TransactionResponseDTO> requestP2P(
            @Parameter(description = "The trading intention id that will be processed", required = true) @PathVariable("intentionId") Long intentionId,
            @Parameter(description = "The user id who wants to operate", required = true) @PathVariable("userId") Long userId) {
        return ResponseEntity.ok(TransactionResponseDTO.fromModel(transactionService.createTransaction(intentionId, userId)));
    }

    @Operation(summary = "Confirms the transaction")
    @PostMapping("/confirm/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> completeTransaction(
            @Parameter(description = "The transaction id that will be completed", required = true) @PathVariable("transactionId") Long intentionId) {
        var response = TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CONFIRM));
        return ResponseEntity
                .status(response.getStatus() == TransactionResponseDTO.TransactionStatus.FAILED ? HttpStatus.BAD_REQUEST : HttpStatus.OK)
                .body(response);
    }

    @Operation(summary = "Cancels the transaction")
    @PostMapping("/cancel/{transactionId}")
    public ResponseEntity<TransactionResponseDTO> cancelTransaction(
            @Parameter(description = "The transaction id that will be canceled", required = true) @PathVariable("transactionId") Long intentionId) {
        return ResponseEntity.ok(TransactionResponseDTO.fromModel(transactionService.processTransaction(intentionId, TransactionAction.CANCEL)));
    }
}
