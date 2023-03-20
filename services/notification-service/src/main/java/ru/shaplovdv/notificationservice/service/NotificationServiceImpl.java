package ru.shaplovdv.notificationservice.service;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplovdv.notificationservice.model.Notification;
import ru.shaplovdv.notificationservice.model.persistence.NotificationEntity;
import ru.shaplovdv.notificationservice.repository.NotificationRepository;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final ModelMapper modelMapper;

    public NotificationServiceImpl(NotificationRepository repository,
                                   ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUserNotifications(Long userId) {
        return modelMapper.map(repository.findByUserId(userId), new TypeToken<List<Notification>>() {
        }.getType());
    }

    @Override
    @Transactional(readOnly = true)
    public Notification getLastUserNotification(Long userId) {
        return modelMapper.map(repository.findFirstByUserIdOrderByDateDesc(userId)
                .orElse(new NotificationEntity()), Notification.class);
    }
}
