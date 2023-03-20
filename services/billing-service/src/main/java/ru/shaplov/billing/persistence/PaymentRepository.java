package ru.shaplov.billing.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplov.billing.model.persistence.PaymentEntity;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    List<PaymentEntity> findAllByAccountUserIdOrderByPaymentDateDesc(Long userId);
}
