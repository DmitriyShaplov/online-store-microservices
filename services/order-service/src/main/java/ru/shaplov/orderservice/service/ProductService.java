package ru.shaplov.orderservice.service;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductService {

    BigDecimal getProductPrice(UUID productId);
}
