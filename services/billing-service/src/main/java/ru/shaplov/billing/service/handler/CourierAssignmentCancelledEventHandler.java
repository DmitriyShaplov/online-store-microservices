package ru.shaplov.billing.service.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.billing.service.PaymentService;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.ErrorDto;
import ru.shaplov.common.model.event.order.OrderEvent;
import ru.shaplov.common.model.event.order.OrderEventPayload;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.outbox.EventPublisher;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CourierAssignmentCancelledEventHandler implements EventHandler<OrderEventPayload> {

    private final PaymentService paymentService;
    private final EventPublisher eventPublisher;


    public CourierAssignmentCancelledEventHandler(PaymentService paymentService,
                                                  EventPublisher eventPublisher) {
        this.paymentService = paymentService;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.COURIER_ASSIGNMENT_CANCELLED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        UUID orderId = UUID.fromString(key);

        paymentService.processPaymentCancellation(orderId);

        ErrorDto error = payload.getError();
        eventPublisher.publishEvent(OrderEvent.of(orderId, error, OrderEventType.PAYMENT_CANCELLED));
        log.debug("payment reverted");
    }
}
