package ru.shaplov.orderservice.service.impl;

import org.springframework.stereotype.Service;
import ru.shaplov.orderservice.service.ProductService;

import java.math.BigDecimal;
import java.util.Random;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final Random rnd = new Random();

    /**
     * Заглушка
     */
    @Override
    public BigDecimal getProductPrice(UUID productId) {
        return BigDecimal.valueOf(rnd.nextDouble(0D, 100_000D));
    }
}
