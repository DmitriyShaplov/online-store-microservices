package ru.shaplovdv.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplovdv.product.model.persistence.ProductEntity;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}
