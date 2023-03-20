package ru.shaplov.common.model.event.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderProduct {

    private UUID productId;

    private BigDecimal currentPrice;

    private Integer quantity;
}
