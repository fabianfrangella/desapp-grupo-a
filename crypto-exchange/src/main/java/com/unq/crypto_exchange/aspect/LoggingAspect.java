package com.unq.crypto_exchange.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Autowired
    private ObjectMapper objectMapper;

    @Around("execution(* com.unq.crypto_exchange.api.controller..*(..))")
    public Object logControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        Object[] args = joinPoint.getArgs();

        try {
            String paramsJson = objectMapper.writeValueAsString(args);
            log.info("Method: {}, Parameters: {}", methodName, paramsJson);
        } catch (Exception e) {
            log.warn("Failed to serialize input parameters for method: {}", methodName, e);
        }

        Object result = joinPoint.proceed();

        Instant end = Instant.now();
        long executionTime = end.toEpochMilli() - start.toEpochMilli();

        try {
            String resultJson = objectMapper.writeValueAsString(result);
            log.info("Method: {}, Execution Time: {} ms, Output: {}", methodName, executionTime, resultJson);
        } catch (Exception e) {
            log.warn("Failed to serialize output for method: {}", methodName, e);
        }

        return result;
    }
}
