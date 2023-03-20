package ru.shaplov.billing.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Payment {
    private UUID id;
    private Long userId;
    private BigDecimal amount;
    private String prefix;
    private String suffix;
    private OffsetDateTime paymentDate;
}
