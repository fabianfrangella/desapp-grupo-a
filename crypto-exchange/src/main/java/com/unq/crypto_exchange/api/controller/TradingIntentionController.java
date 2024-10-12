package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.TradingIntentionDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseListDTO;
import com.unq.crypto_exchange.service.TradingIntentionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/intention")
public class TradingIntentionController {

    private final TradingIntentionService tradingIntentionService;

    @Operation(summary = "Creates a trading intention by user")
    @PostMapping("/{userId}")
    public ResponseEntity<TradingIntentionResponseListDTO> publishTradingIntention(
            @Parameter(description = "The user's id who creating the intent", required = true) @PathVariable("userId") Long userId,
            @RequestBody TradingIntentionDTO tradingIntentionDTO) {
        return new ResponseEntity<>(
                TradingIntentionResponseListDTO.fromModel(
                        tradingIntentionService.publishTradingIntention(userId, tradingIntentionDTO.toModel())),
                        HttpStatus.CREATED
        );
    }

    @Operation(summary = "Get all trading intentions")
    @GetMapping("/find")
    public ResponseEntity<List<TradingIntentionResponseListDTO>> find() {
        return new ResponseEntity<>(
                TradingIntentionResponseListDTO.fromModel(
                        tradingIntentionService.find()),
                HttpStatus.OK
        );
    }

    @Operation(summary = "Get a specific trading intention")
    @GetMapping("/find/{id}")
    public ResponseEntity<TradingIntentionResponseDTO> findById(
            @Parameter(description = "The trading intention id that needs to be fetched", required = true) @PathVariable("id") Long id) {
        return new ResponseEntity<>(
                TradingIntentionResponseDTO.fromModel(
                        tradingIntentionService.findById(id)),
                HttpStatus.OK
        );
    }
}
