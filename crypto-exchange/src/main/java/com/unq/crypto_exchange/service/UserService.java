package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.UserRepository;
import com.unq.crypto_exchange.security.PasswordEncoderFactory;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public CryptoUser register(CryptoUser user) {
        var maybeUser = userRepository.findByEmail(user.getEmail());
        maybeUser.ifPresent((existentUser) -> {
            logger.info("User email {} already exists", user.getEmail());
            throw new UserAlreadyExistException(user.getEmail());
        });
        var encryptedPassword = PasswordEncoderFactory.getDefaultEncoder().encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
        return user;
    }
}
