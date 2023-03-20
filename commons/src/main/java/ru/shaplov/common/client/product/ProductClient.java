package ru.shaplov.common.client.product;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.shaplov.common.client.product.model.Product;
import ru.shaplov.common.client.product.model.ProductParam;

import java.util.List;

@ConditionalOnProperty(name = "clients.products.url")
@FeignClient(value = "jplaceholder", url = "${clients.products.url}")
public interface ProductClient {

    @PostMapping("/api/v1/internal/products/search")
    List<Product> getProductList(@RequestBody ProductParam param);
}
