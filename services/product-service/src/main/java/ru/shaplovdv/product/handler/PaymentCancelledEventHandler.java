package ru.shaplovdv.product.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.order.OrderEvent;
import ru.shaplov.common.model.event.order.OrderEventPayload;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.outbox.EventPublisher;
import ru.shaplovdv.product.service.ProductsOrderService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class PaymentCancelledEventHandler implements EventHandler<OrderEventPayload> {

    private final ProductsOrderService productsOrderService;
    private final EventPublisher eventPublisher;

    public PaymentCancelledEventHandler(ProductsOrderService productsOrderService,
                                        EventPublisher eventPublisher) {
        this.productsOrderService = productsOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.PAYMENT_CANCELLED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        UUID orderId = UUID.fromString(key);
        productsOrderService.processOrderCancellation(orderId);
        eventPublisher.publishEvent(OrderEvent.of(orderId, payload.getError(), OrderEventType.PRODUCTS_RESERVATION_CANCELLED));
    }
}
