package ru.shaplov.billing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.billing.model.persistence.TransactionEntity;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findAllByAccountUserIdOrderByDateDesc(Long userId);
    List<TransactionEntity> findAllByOrderIdOrderByDateDesc(UUID orderId);
    TransactionEntity getByOrderId(UUID orderId);
}
