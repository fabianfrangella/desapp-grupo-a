package com.unq.crypto_exchange.repository;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<CryptoUser, Long> {
    Optional<CryptoUser> findByEmail(@Param("email") String email);
}
