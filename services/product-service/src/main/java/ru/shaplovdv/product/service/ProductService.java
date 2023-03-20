package ru.shaplovdv.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.shaplovdv.product.model.Product;
import ru.shaplovdv.product.model.ProductParam;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    void uploadProductsByFile(InputStream inputStream);

    Product uploadProduct(Product product);

    Page<Product> getProductsPage(Pageable pageable);

    List<Product> getProductList(Sort sort);

    Product getProduct(UUID uuid);

    List<Product> searchProducts(ProductParam param);
}
