package ru.shaplov.common.model.event.notification;

import ru.shaplov.common.model.event.AggregateType;
import ru.shaplov.common.outbox.ExportedEvent;

public class NotificationEvent implements ExportedEvent<NotificationPayload> {

    private final NotificationPayload payload;
    private final String aggregateId;

    private NotificationEvent(NotificationPayload payload) {
        this.payload = payload;
        this.aggregateId = payload.getOrderId().toString();
    }

    public static NotificationEvent of(NotificationPayload payload) {
        return new NotificationEvent(payload);
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return AggregateType.NOTIFICATION.getValue();
    }

    @Override
    public NotificationPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return NotificationEventType.USER_NOTIFICATION.name();
    }
}
