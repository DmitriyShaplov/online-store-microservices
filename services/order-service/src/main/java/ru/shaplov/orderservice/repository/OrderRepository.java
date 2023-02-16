package ru.shaplov.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.orderservice.model.persistence.OrderEntity;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}
