package ru.shaplovdv.product.handler;

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
import ru.shaplovdv.product.service.ProductsOrderService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderCreatedEventHandler implements EventHandler<OrderEventPayload> {

    private final ProductsOrderService productsOrderService;
    private final EventPublisher eventPublisher;

    public OrderCreatedEventHandler(ProductsOrderService productsOrderService,
                                    EventPublisher eventPublisher) {
        this.productsOrderService = productsOrderService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.ORDER_CREATED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        OrderDto order = payload.getOrderData();
        productsOrderService.processOrderReservation(order);
        eventPublisher.publishEvent(OrderEvent.of(order, OrderEventType.PRODUCTS_RESERVED));
        log.debug("products reserved");
    }

    @Override
    public boolean hasErrorProcessing() {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleError(Exception e, String key, OrderEventPayload payload, String orderEventType) {
        log.error("failed reserve products! {}", e.getMessage(), e);
        eventPublisher.publishEvent(OrderEvent.of(UUID.fromString(key),
                new ErrorDto("Неудачное резервирование продуктов", e.getMessage()),
                OrderEventType.PRODUCTS_RESERVATION_CANCELLED));
    }
}
