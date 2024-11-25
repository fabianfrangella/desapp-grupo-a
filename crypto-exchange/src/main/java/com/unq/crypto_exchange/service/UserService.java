package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.api.dto.OperatedCryptoDTO;
import com.unq.crypto_exchange.api.dto.UserResponseDto;
import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.external.service.DolarapiExternalService;
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
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final CryptoPriceRepository cryptoPriceRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final DolarapiExternalService dolarapiExternalService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<CryptoUser> findAll() {
        return userRepository.findAll();
    }
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
        var cryptoDollarPrice = dolarapiExternalService.getCryptoDollar().getVenta();
        var cryptos = operatedCryptos.stream()
                .map(crypto -> {
                    var price = cryptoPriceRepository.findFirstByCryptoCurrencyTypeOrderByTimeDesc(crypto.getType()).get();
                    var arsTotalValue = BigDecimal.valueOf(crypto.getQuantity())
                            .multiply(price.getPrice())
                            .divide(cryptoDollarPrice, RoundingMode.FLOOR);
                    return OperatedCryptoDTO.CryptoActiveDTO.fromModel(crypto, price, arsTotalValue);
                })
                .toList();

        return new OperatedCryptoDTO(LocalDateTime.now(),
                cryptos.stream()
                        .map(crypto -> crypto.price().getPrice().multiply(BigDecimal.valueOf(crypto.quantity())))
                        .reduce(BigDecimal.ZERO, (a, b) -> b.plus()),
                cryptos.stream()
                        .map(crypto -> crypto.arsValue().multiply(BigDecimal.valueOf(crypto.quantity())))
                        .reduce(BigDecimal.ZERO, (a, b) -> b.plus()),
                cryptos);
    }
}
