package ru.shaplovdv.product.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplovdv.product.model.persistence.OrderEntity;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @EntityGraph(attributePaths = "productList")
    Optional<OrderEntity> getByOrderId(UUID orderId);
}
