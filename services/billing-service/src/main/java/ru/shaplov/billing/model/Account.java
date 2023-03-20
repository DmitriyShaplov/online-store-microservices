package ru.shaplov.billing.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Account {
    private Long userId;
    private BigDecimal balance;
}
