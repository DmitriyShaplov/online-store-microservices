package ru.shaplov.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CartItem {

    @JsonIgnore
    private Long userId;
    @NotNull
    private UUID productId;
    @NotNull
    private Integer quantity;
}
