package com.unq.crypto_exchange.service;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import com.unq.crypto_exchange.repository.UserRepository;
import com.unq.crypto_exchange.service.exception.UserAlreadyExistException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    void registerUserSuccessfully() {
        // Arrange
        CryptoUser user = new CryptoUser();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        // Act
        CryptoUser registeredUser = userService.register(user);

        // Assert
        assertNotNull(registeredUser);
        verify(userRepository).save(user);
    }


    @Test
    void registerUserAlreadyExists() {
        // Arrange
        CryptoUser user = new CryptoUser();
        user.setEmail("test@example.com");
        user.setPassword("password");
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        // Act & Assert
        assertThrows(UserAlreadyExistException.class, () -> userService.register(user));
        verify(userRepository, never()).save(any());
    }
}