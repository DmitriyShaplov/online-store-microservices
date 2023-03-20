package ru.shaplovdv.notificationservice.service;

import org.springframework.stereotype.Component;
import ru.shaplov.common.model.event.notification.NotificationPayload;
import ru.shaplovdv.notificationservice.model.OrderStatus;

@Component
public class NotificationMessageBuilder {

    public String buildMessage(NotificationPayload notificationPayload) {
        if (notificationPayload.getOrderStatus().equals(OrderStatus.CREATED.name())) {
            return buildCreated(notificationPayload);
        } else if (notificationPayload.getOrderStatus().equals(OrderStatus.CANCELLED.name())) {
            return buildCancelled(notificationPayload);
        } else if (notificationPayload.getOrderStatus().equals(OrderStatus.FINISHED.name())) {
            return buildFinished(notificationPayload);
        }
        return null;
    }

    private String buildCreated(NotificationPayload notificationPayload) {
        return String.format("Ордер %s был создан.%nОжидайте по адресу%s %s",
                notificationPayload.getOrderId(),
                notificationPayload.getShippingAddress(),
                notificationPayload.getDeliveryDate());
    }

    private String buildCancelled(NotificationPayload notificationPayload) {
        return String.format("Ордер %s был отменен. %s",
                notificationPayload.getOrderId(),
                notificationPayload.getErrorDto().getMessage());
    }

    private String buildFinished(NotificationPayload notificationPayload) {
        return String.format("Заказ %s был успешно доставлен. Спасибо что пользуетесь нашим сервисом",
                notificationPayload.getOrderId());
    }

}
