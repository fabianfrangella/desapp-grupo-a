package com.unq.crypto_exchange.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = {"type", "user_id"})
})
public class CryptoActive extends EntityMetaData {

    @ManyToOne(cascade = CascadeType.ALL)
    private CryptoUser user;
    private CryptoCurrencyType type;
    private Long quantity;

    public void addQuantity(Long quantity) {
        this.quantity+= quantity;
    }

    public void removeQuantity(Long quantity) {
        this.quantity-= quantity;
    }
}
