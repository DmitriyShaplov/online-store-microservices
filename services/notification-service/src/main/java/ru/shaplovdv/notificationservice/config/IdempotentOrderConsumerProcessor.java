package ru.shaplovdv.notificationservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.consumer.IdempotentConsumerProcessor;
import ru.shaplov.common.model.event.notification.NotificationPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IdempotentOrderConsumerProcessor extends IdempotentConsumerProcessor<NotificationPayload> {


    private final Map<String, EventHandler<NotificationPayload>> eventProcessorServiceMap;

    public IdempotentOrderConsumerProcessor(JdbcTemplate jdbcTemplate,
                                            List<EventHandler<NotificationPayload>> processorServices,
                                            TransactionTemplate transactionTemplate) {
        super(jdbcTemplate, transactionTemplate);
        Map<String, EventHandler<NotificationPayload>> processorServiceMap = new HashMap<>();
        for (EventHandler<NotificationPayload> processorService : processorServices) {
            for (String eventType : processorService.eventTypes()) {
                processorServiceMap.merge(eventType, processorService, (oldValue, value) -> {
                    throw new IllegalStateException("Несколько обработчиков на одно событие недопустимо!");
                });
            }
        }
        this.eventProcessorServiceMap = processorServiceMap;
    }

    @Override
    protected EventHandler<NotificationPayload> eventProcessorService(String orderEventType) {
        return eventProcessorServiceMap.get(orderEventType);
    }
}
