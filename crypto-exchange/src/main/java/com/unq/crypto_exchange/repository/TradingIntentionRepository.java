package com.unq.crypto_exchange.repository;

import com.unq.crypto_exchange.domain.entity.TradingIntention;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingIntentionRepository extends JpaRepository<TradingIntention, Long> {
    List<TradingIntention> findAllByStatus(TradingIntention.Status status);
}
