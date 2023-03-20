package ru.shaplovdv.product.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplovdv.product.model.Product;
import ru.shaplovdv.product.model.ProductParam;
import ru.shaplovdv.product.service.ProductService;

import java.util.List;

@RestController
public class ProductInternalController {

    private final ProductService productService;

    public ProductInternalController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/v1/internal/products/search")
    public List<Product> getProductList(@RequestBody ProductParam param) {
        return productService.searchProducts(param);
    }
}
