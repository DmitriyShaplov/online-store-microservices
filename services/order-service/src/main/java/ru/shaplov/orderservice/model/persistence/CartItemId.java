package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
public class CartItemId implements Serializable {

    private Long userId;

    private UUID productId;
}
