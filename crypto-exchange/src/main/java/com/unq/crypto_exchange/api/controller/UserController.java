package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.RegisterUserDTO;
import com.unq.crypto_exchange.api.dto.RegisterUserResponseDTO;
import com.unq.crypto_exchange.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Registers a user")
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        var user = userService.register(registerUserDTO.toModel());
        return new ResponseEntity<>(RegisterUserResponseDTO.fromModel(user), HttpStatus.CREATED);
    }
}
