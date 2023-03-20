package ru.shaplov.common.consumer.order;

import org.springframework.context.annotation.Conditional;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import ru.shaplov.common.consumer.IdempotentConsumerProcessor;
import ru.shaplov.common.model.event.order.OrderEventPayload;

@Component
@Conditional(OrderConsumerCondition.class)
public class OrderEventConsumer {

    private final IdempotentConsumerProcessor<OrderEventPayload> consumerProcessor;

    public OrderEventConsumer(IdempotentConsumerProcessor<OrderEventPayload> consumerProcessor) {
        this.consumerProcessor = consumerProcessor;
    }

    @KafkaListener(topics = "outbox.event.order", containerFactory = "orderContainerFactory")
    public void processMessage(OrderEventPayload payload,
                               @Header(name = "id") String id,
                               @Header(name = "type") String orderEventType,
                               @Header(name = KafkaHeaders.RECEIVED_KEY) String key) {
        consumerProcessor.processMessage(key, payload, id, orderEventType);
    }
}
