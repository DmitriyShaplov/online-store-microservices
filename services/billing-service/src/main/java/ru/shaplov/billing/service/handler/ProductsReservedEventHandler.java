package ru.shaplov.billing.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.billing.service.PaymentService;
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
public class ProductsReservedEventHandler implements EventHandler<OrderEventPayload> {

    private final PaymentService paymentService;
    private final EventPublisher eventPublisher;

    public ProductsReservedEventHandler(PaymentService paymentService,
                                        EventPublisher eventPublisher) {
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.PRODUCTS_RESERVED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        OrderDto order = payload.getOrderData();
        paymentService.processPayment(order);
        eventPublisher.publishEvent(OrderEvent.of(order, OrderEventType.PAYMENT_PROCESSED));
        log.debug("payment processed");
    }

    @Override
    public boolean hasErrorProcessing() {
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void handleError(Exception e, String key, OrderEventPayload payload, String orderEventType) {
        log.error("failed process payment! {}", e.getMessage(), e);
        eventPublisher.publishEvent(OrderEvent.of(UUID.fromString(key),
                new ErrorDto("Неудачная оплата, не хватает средств, либо системная ошибка",
                        e.getMessage()),
                OrderEventType.PAYMENT_CANCELLED));
    }
}
