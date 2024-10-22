package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.OperatedCryptoDTO;
import com.unq.crypto_exchange.service.UserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@AllArgsConstructor
@RestController
@Transactional
@RequestMapping("/cryptoactive")
public class CryptoActiveController {

    private final UserService userService;

    @GetMapping("/operated/{userId}")
    public ResponseEntity<OperatedCryptoDTO> findOperatedCryptoBetween(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
                                                                       @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
                                                                       @PathVariable("userId") Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findOperatedCryptoBetween(from, to, userId));
    }
}
