package com.unq.crypto_exchange.api.dto;

import com.unq.crypto_exchange.domain.entity.OperationType;

public enum OperationTypeDTO {
    PURCHASE,
    SALE,
    CANCEL;

    public OperationType toModel() {
        return switch (this) {
            case PURCHASE -> OperationType.PURCHASE;
            case SALE -> OperationType.SALE;
            case CANCEL -> OperationType.CANCEL;
        };
    }

    public static OperationTypeDTO fromModel(OperationType operationType) {
        return switch (operationType) {
            case PURCHASE -> OperationTypeDTO.PURCHASE;
            case SALE -> OperationTypeDTO.SALE;
            case CANCEL, SYSTEM_CANCEL -> OperationTypeDTO.CANCEL;
        };
    }
}
