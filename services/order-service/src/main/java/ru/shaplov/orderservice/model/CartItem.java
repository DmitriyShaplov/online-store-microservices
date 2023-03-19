package ru.shaplov.orderservice.model;

import lombok.Data;

import java.util.UUID;

@Data
public class CartItem {

    private Long userId;
    private UUID productId;
    private Integer quantity;
}
