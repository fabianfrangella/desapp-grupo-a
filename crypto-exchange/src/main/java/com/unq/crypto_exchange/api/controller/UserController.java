package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.RegisterUserDTO;
import com.unq.crypto_exchange.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterUserDTO> register(@RequestBody RegisterUserDTO registerUserDTO) {
        userService.register(registerUserDTO.toModel());
        return new ResponseEntity<>(registerUserDTO, HttpStatus.CREATED);
    }
}
