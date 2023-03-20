package ru.shaplov.common.outbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(ExportedEvent<?> event) {
        applicationEventPublisher.publishEvent(event);
        log.info("Published event. {}, {}, {}", event.getAggregateType(), event.getAggregateId(), event.getType());
    }
}
