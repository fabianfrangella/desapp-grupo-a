package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.CryptoPriceDTO;
import com.unq.crypto_exchange.service.CryptoPriceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/cryptoprice")
public class CryptoPriceController {

    private final CryptoPriceService cryptoPriceService;

    @Operation(summary = "Get all cryptocurrency prices")
    @GetMapping("/find")
    public ResponseEntity<List<CryptoPriceDTO>> find() {
        return new ResponseEntity<>(
                CryptoPriceDTO.fromModel(
                        cryptoPriceService.find()),
                HttpStatus.OK
        );
    }
}
