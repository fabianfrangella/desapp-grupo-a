package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.api.dto.OperatedCryptoDTO;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.CryptoPriceRepository;
import com.unq.crypto_exchange.repository.UserRepository;
import com.unq.crypto_exchange.service.exception.UserAlreadyExistException;
import com.unq.crypto_exchange.service.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CryptoPriceRepository cryptoPriceRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public CryptoUser register(CryptoUser user) {
        var maybeUser = userRepository.findByEmail(user.getEmail());
        maybeUser.ifPresent(existentUser -> {
            logger.info("User email {} already exists", user.getEmail());
            throw new UserAlreadyExistException(user.getEmail());
        });
        var encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.fillInitialWallet();
        userRepository.save(user);
        return user;
    }

    public OperatedCryptoDTO findOperatedCryptoBetween(LocalDate from, LocalDate to, Long userId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User with id " + userId + " not found"));
        var operatedCryptos = user.findCryptoActivesOperatedBetween(from, to);
        var cryptos = operatedCryptos.stream()
                .map(crypto -> OperatedCryptoDTO.CryptoActiveDTO.fromModel(crypto,
                        cryptoPriceRepository.findFirstByCryptoCurrencyTypeOrderByTimeDesc(crypto.getType()).get()))
                .toList();
        return new OperatedCryptoDTO(LocalDateTime.now(), BigDecimal.ZERO, BigDecimal.ZERO, cryptos);
    }
}
