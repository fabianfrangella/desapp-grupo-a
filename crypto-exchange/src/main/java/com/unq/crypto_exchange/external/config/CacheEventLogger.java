package com.unq.crypto_exchange.external.config;

import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheEventLogger implements CacheEventListener<CryptoCurrencyType, List<CryptoPrice>> {
    private final Logger log = LoggerFactory.getLogger(CacheEventLogger.class);

    @Override
    public void onEvent(CacheEvent<? extends CryptoCurrencyType, ? extends List<CryptoPrice>> cacheEvent) {
        log.info("CacheLogger Triggered", cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}