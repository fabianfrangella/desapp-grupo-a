package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public CryptoUser register(CryptoUser user) {
        return userRepository.save(user);
    }
}
