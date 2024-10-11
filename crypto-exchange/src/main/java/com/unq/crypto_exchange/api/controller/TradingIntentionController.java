package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.TradingIntentionDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseDTO;
import com.unq.crypto_exchange.api.dto.TradingIntentionResponseListDTO;
import com.unq.crypto_exchange.service.TradingIntentionService;
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

    @PostMapping("/{userId}")
    public ResponseEntity<TradingIntentionResponseListDTO> publishTradingIntention(@PathVariable("userId") Long userId,
                                                                                   @RequestBody TradingIntentionDTO tradingIntentionDTO) {
        return new ResponseEntity<>(
                TradingIntentionResponseListDTO.fromModel(
                        tradingIntentionService.publishTradingIntention(userId, tradingIntentionDTO.toModel())),
                        HttpStatus.CREATED
        );
    }

    @GetMapping("/find")
    public ResponseEntity<List<TradingIntentionResponseListDTO>> find() {
        return new ResponseEntity<>(
                TradingIntentionResponseListDTO.fromModel(
                        tradingIntentionService.find()),
                HttpStatus.OK
        );
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<TradingIntentionResponseDTO> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                TradingIntentionResponseDTO.fromModel(
                        tradingIntentionService.findById(id)),
                HttpStatus.OK
        );
    }
}
