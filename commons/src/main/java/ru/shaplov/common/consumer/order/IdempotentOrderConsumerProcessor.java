package ru.shaplov.common.consumer.order;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Conditional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.consumer.IdempotentConsumerProcessor;
import ru.shaplov.common.model.event.order.OrderEventPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@Conditional(OrderConsumerCondition.class)
public class IdempotentOrderConsumerProcessor extends IdempotentConsumerProcessor<OrderEventPayload> {


    private final Map<String, EventHandler<OrderEventPayload>> eventProcessorServiceMap;

    public IdempotentOrderConsumerProcessor(JdbcTemplate jdbcTemplate,
                                            List<EventHandler<OrderEventPayload>> processorServices,
                                            TransactionTemplate transactionTemplate) {
        super(jdbcTemplate, transactionTemplate);
        Map<String, EventHandler<OrderEventPayload>> processorServiceMap = new HashMap<>();
        for (EventHandler<OrderEventPayload> processorService : processorServices) {
            for (String eventType : processorService.eventTypes()) {
                processorServiceMap.merge(eventType, processorService, (oldValue, value) -> {
                    throw new IllegalStateException("Несколько обработчиков на одно событие недопустимо!");
                });
            }
        }
        this.eventProcessorServiceMap = processorServiceMap;
    }

    @Override
    protected EventHandler<OrderEventPayload> eventProcessorService(String orderEventType) {
        return eventProcessorServiceMap.get(orderEventType);
    }
}
