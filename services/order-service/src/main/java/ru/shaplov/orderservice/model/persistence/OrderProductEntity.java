package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Embeddable
public class OrderProductEntity {

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "current_price")
    private BigDecimal currentPrice;

    private int quantity;
}
