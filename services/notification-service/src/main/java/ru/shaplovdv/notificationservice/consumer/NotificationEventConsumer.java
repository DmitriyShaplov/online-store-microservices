package ru.shaplovdv.notificationservice.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.shaplov.common.consumer.IdempotentConsumerProcessor;
import ru.shaplov.common.model.event.notification.NotificationPayload;

@Component
public class NotificationEventConsumer {

    private final IdempotentConsumerProcessor<NotificationPayload> consumerProcessor;

    public NotificationEventConsumer(IdempotentConsumerProcessor<NotificationPayload> consumerProcessor) {
        this.consumerProcessor = consumerProcessor;
    }

    @KafkaListener(topics = "outbox.event.notification", containerFactory = "notificationContainerFactory")
    public void processMessage(NotificationPayload payload,
                               @Header(name = "id") String id,
                               @Header(name = "type") String orderEventType,
                               @Header(name = KafkaHeaders.RECEIVED_KEY) String key) {
        consumerProcessor.processMessage(key, payload, id, orderEventType);
    }
}
