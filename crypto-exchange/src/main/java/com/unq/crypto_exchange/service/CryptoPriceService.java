package com.unq.crypto_exchange.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.unq.crypto_exchange.api.dto.CryptoPriceDTO;
import com.unq.crypto_exchange.domain.entity.CryptoCurrencyType;
import com.unq.crypto_exchange.domain.entity.CryptoPrice;
import com.unq.crypto_exchange.external.dto.BinancePriceResponse;
import com.unq.crypto_exchange.external.service.BinanceExternalService;
import com.unq.crypto_exchange.repository.CryptoPriceRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CryptoPriceService {
    private final CryptoPriceRepository cryptoPriceRepository;
    private final BinanceExternalService binanceExternalService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CACHE_KEY = "cryptoPrices";

    public List<CryptoPrice> find() {
        return cryptoPriceRepository.findLatestCryptoPrices();
    }

    public List<CryptoPrice> findLast() {
        List<CryptoPrice> cachedPrices = getCachedPrices();
        if (cachedPrices != null && !cachedPrices.isEmpty()) {
            return cachedPrices;
        }

        List<CryptoPrice> freshPrices = binanceExternalService.getPrices(Arrays.asList(CryptoCurrencyType.values())).stream().map(BinancePriceResponse::toModel).toList();

        updateCache(freshPrices);
        updateDatabase(freshPrices);

        return freshPrices;
    }

    public List<CryptoPriceDTO> getConsolidatedPrices(CryptoCurrencyType cryptoCurrencyType) {
        List<CryptoPrice> prices = cryptoPriceRepository.findAllPricesFromLast24HoursByType(cryptoCurrencyType, LocalDateTime.now().minusHours(24));

        Map<LocalDateTime, List<CryptoPrice>> groupedByHour = prices.stream()
                .collect(Collectors.groupingBy(price -> price.getTime().withMinute(0).withSecond(0).withNano(0)));

        return groupedByHour.entrySet().stream()
                .map(entry -> CryptoPriceDTO.builder()
                        .cryptoCurrencyType(cryptoCurrencyType)
                        .time(entry.getKey())
                        .price(entry.getValue().getLast().getPrice())
                        .build())
                .sorted(Comparator.comparing(CryptoPriceDTO::getTime))
                .toList();

    }

    private List<CryptoPrice> getCachedPrices() {
        Object cachedData = redisTemplate.opsForValue().get(CACHE_KEY);
        if (cachedData != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return mapper.convertValue(cachedData, new TypeReference<List<CryptoPrice>>() {});
        }
        return null;
    }


    private void updateCache(List<CryptoPrice> prices) {
        redisTemplate.opsForValue().set(CACHE_KEY, prices, 600, TimeUnit.SECONDS);
    }

    private void updateDatabase(List<CryptoPrice> prices) {
        for (CryptoPrice price : prices) {
            cryptoPriceRepository.save(price);
        }
    }
}