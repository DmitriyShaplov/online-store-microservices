package ru.shaplovdv.product.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.order.OrderEventPayload;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplovdv.product.service.ProductsOrderService;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InDeliveryEventHandler implements EventHandler<OrderEventPayload> {

    private final ProductsOrderService productsOrderService;

    public InDeliveryEventHandler(ProductsOrderService productsOrderService) {
        this.productsOrderService = productsOrderService;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.IN_DELIVERY.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        UUID orderId = UUID.fromString(key);
        productsOrderService.finishOrderProcessing(orderId);
    }
}
