package ru.shaplov.orderservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderProduct {

    private UUID productId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal currentPrice;

    private Integer quantity;
}
