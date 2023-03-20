package ru.shaplov.orderservice.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.orderservice.model.persistence.OrderEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @EntityGraph(attributePaths = "products")
    Optional<OrderEntity> findOrderEntityById(UUID id);

    boolean existsByIdAndUserId(UUID id, Long accountId);

    List<OrderEntity> findAllByUserId(Long userId);
}
