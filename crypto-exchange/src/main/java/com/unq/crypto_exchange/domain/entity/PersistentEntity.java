package com.unq.crypto_exchange.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class PersistentEntity {
    protected LocalDateTime deletedAt;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
}
