package ru.shaplovdv.notificationservice.service;

import ru.shaplovdv.notificationservice.model.Notification;

import java.util.List;

public interface NotificationService {

    List<Notification> getUserNotifications(Long userId);
    Notification getLastUserNotification(Long userId);
}
