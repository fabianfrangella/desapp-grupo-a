package com.unq.crypto_exchange.repository;

import com.unq.crypto_exchange.domain.entity.CryptoUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<CryptoUser, Long> {
}
