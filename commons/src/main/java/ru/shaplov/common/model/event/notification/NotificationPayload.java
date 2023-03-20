package ru.shaplov.common.model.event.notification;

import lombok.Data;
import ru.shaplov.common.model.event.ErrorDto;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class NotificationPayload {
    private String email;
    private UUID orderId;
    private Long userId;
    private OffsetDateTime deliveryDate;
    private ErrorDto errorDto;
    private String shippingAddress;
    private String orderStatus;
}
