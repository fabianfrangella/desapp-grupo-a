package com.unq.crypto_exchange.security;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderFactory {

    public static PasswordEncoder getDefaultEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
