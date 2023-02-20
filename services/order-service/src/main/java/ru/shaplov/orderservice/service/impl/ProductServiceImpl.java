package ru.shaplov.orderservice.service.impl;

import org.springframework.stereotype.Service;
import ru.shaplov.orderservice.service.ProductService;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductServiceImpl implements ProductService {

    /**
     * Заглушка
     */
    @Override
    public BigDecimal getProductPrice(UUID productId) {
        return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0D, 100_000D));
    }
}
