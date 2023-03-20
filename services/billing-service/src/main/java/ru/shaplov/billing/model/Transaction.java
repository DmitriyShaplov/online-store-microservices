package ru.shaplov.billing.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Transaction {
    private Long id;
    private Long userId;
    private UUID orderId;
    private TransactionType type;
    private BigDecimal amount;
    private OffsetDateTime date;
}
