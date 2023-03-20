package ru.shaplovdv.notificationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shaplovdv.notificationservice.model.persistence.NotificationEntity;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByUserId(Long userId);
    Optional<NotificationEntity> findFirstByUserIdOrderByDateDesc(Long userId);
}
