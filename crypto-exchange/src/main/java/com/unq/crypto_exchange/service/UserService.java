package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public CryptoUser register(CryptoUser user) {
        var maybeUser = userRepository.findByEmail(user.getEmail());
        maybeUser.ifPresent((existentUser) -> {
            throw new UserAlreadyExistException(user.getEmail());
        });
        userRepository.save(user);
        return user;
    }
}
