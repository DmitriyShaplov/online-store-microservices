package com.example.deliveryservice.handler;

import com.example.deliveryservice.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.ErrorDto;
import ru.shaplov.common.model.event.order.OrderDto;
import ru.shaplov.common.model.event.order.OrderEvent;
import ru.shaplov.common.model.event.order.OrderEventPayload;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.outbox.EventPublisher;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentProcessedEventHandler implements EventHandler<OrderEventPayload> {

    private final CourierService courierService;
    private final EventPublisher eventPublisher;

    public PaymentProcessedEventHandler(CourierService courierService,
                                        EventPublisher eventPublisher) {
        this.courierService = courierService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.PAYMENT_PROCESSED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        OrderDto order = payload.getOrderData();
        OrderDto orderDto = courierService.attemptToReserveDeliverySlot(order);
        eventPublisher.publishEvent(OrderEvent.of(orderDto, OrderEventType.IN_DELIVERY));
        log.debug("courier assignment");
    }

    @Override
    public boolean hasErrorProcessing() {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleError(Exception e, String key, OrderEventPayload payload, String orderEventType) {
        log.error("failed assignment courier! {}", e.getMessage(), e);
        eventPublisher.publishEvent(OrderEvent.of(UUID.fromString(key),
                new ErrorDto("Не удалось найти курьера на доставку на выбранный интервал времени",
                        e.getMessage()),
                OrderEventType.COURIER_ASSIGNMENT_CANCELLED));
    }
}
