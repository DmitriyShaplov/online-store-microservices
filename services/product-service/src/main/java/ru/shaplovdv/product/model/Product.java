package ru.shaplovdv.product.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class Product {
    private UUID id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private int reserved;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;
}
