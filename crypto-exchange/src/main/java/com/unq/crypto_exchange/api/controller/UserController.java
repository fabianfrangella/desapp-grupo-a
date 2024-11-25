package com.unq.crypto_exchange.api.controller;

import com.unq.crypto_exchange.api.dto.*;
import com.unq.crypto_exchange.service.AuthenticationService;
import com.unq.crypto_exchange.service.JwtService;
import com.unq.crypto_exchange.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Transactional
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    @Operation(summary = "Registers a user")
    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDTO> register(@Valid @RequestBody RegisterUserDTO registerUserDTO) {
        var user = userService.register(registerUserDTO.toModel());
        return new ResponseEntity<>(RegisterUserResponseDTO.fromModel(user), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestBody LoginUserDto loginUserDto) {
        var authenticatedUser = authenticationService.authenticate(loginUserDto);
        var jwtToken = jwtService.generateToken(authenticatedUser);
        var loginResponse = new LoginResponseDto(jwtToken, jwtService.getExpirationTime());
        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> listAllUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(UserResponseDto::fromModel).toList());
    }
}
