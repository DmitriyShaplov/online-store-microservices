package ru.shaplovdv.notificationservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.common.util.SecurityUtil;
import ru.shaplovdv.notificationservice.model.Notification;
import ru.shaplovdv.notificationservice.service.NotificationService;

import java.util.List;

@RestController
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/api/v1/notifications")
    public List<Notification> getUserNotifications() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return notificationService.getUserNotifications(currentUserId);
    }

    @GetMapping("/api/v1/notifications/last")
    public Notification getLastUserNotification() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        return notificationService.getLastUserNotification(currentUserId);
    }
}
