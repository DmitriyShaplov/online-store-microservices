package ru.shaplovdv.notificationservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.notification.NotificationEventType;
import ru.shaplov.common.model.event.notification.NotificationPayload;
import ru.shaplovdv.notificationservice.service.NotificationMessageBuilder;

import java.util.List;

@Service
@Slf4j
public class NotificationEventHandler implements EventHandler<NotificationPayload> {

    private final JdbcTemplate jdbcTemplate;
    private final NotificationMessageBuilder notificationMessageBuilder;

    public NotificationEventHandler(JdbcTemplate jdbcTemplate, NotificationMessageBuilder notificationMessageBuilder) {
        this.jdbcTemplate = jdbcTemplate;
        this.notificationMessageBuilder = notificationMessageBuilder;
    }

    @Override
    public void processEvent(String key, NotificationPayload payload, String orderEventType) {
        String message = notificationMessageBuilder.buildMessage(payload);
        if (message == null) {
            log.error("SYSTEM ERROR. No message builder");
            return;
        }
        jdbcTemplate.update("""
                    INSERT INTO notifications (email, message, user_id, order_id) VALUES
                    (?, ?, ?, ?)
                """, payload.getEmail(), message, payload.getUserId(), payload.getOrderId());
        log.debug("Message is ready to process. {}", key);
    }

    @Override
    public List<String> eventTypes() {
        return List.of(NotificationEventType.USER_NOTIFICATION.name());
    }
}
