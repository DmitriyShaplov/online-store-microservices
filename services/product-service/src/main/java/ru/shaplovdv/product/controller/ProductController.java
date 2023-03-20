package ru.shaplovdv.product.controller;

import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shaplovdv.product.model.Product;
import ru.shaplovdv.product.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //TODO   @PreAuthorize("hasAuthority('SCOPE_admin')")
    @PostMapping("/api/v1/products/upload")
    @SneakyThrows
    public void uploadProductsByFile(@RequestPart MultipartFile file) {
        productService.uploadProductsByFile(file.getInputStream());
    }

    @PostMapping("/api/v1/products")
    public Product uploadProduct(@RequestBody Product product) {
        return productService.uploadProduct(product);
    }

    @GetMapping("/api/v1/products")
    public Page<Product> getProductsPage(@PageableDefault(sort = "updateDate", direction = Sort.Direction.DESC)
                                         Pageable pageable) {
        return productService.getProductsPage(pageable);
    }

    @GetMapping("/api/v1/products/list")
    public List<Product> getProductList(@SortDefault(sort = "updateDate", direction = Sort.Direction.DESC)
                                        Sort sort) {
        return productService.getProductList(sort);
    }

    @GetMapping("/api/v1/products/{uuid}")
    public Product getProduct(@PathVariable UUID uuid) {
        return productService.getProduct(uuid);
    }
}
