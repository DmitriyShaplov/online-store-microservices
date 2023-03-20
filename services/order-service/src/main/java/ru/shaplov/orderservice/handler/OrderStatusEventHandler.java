package ru.shaplov.orderservice.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.common.consumer.EventHandler;
import ru.shaplov.common.model.event.ErrorDto;
import ru.shaplov.common.model.event.notification.NotificationEvent;
import ru.shaplov.common.model.event.notification.NotificationPayload;
import ru.shaplov.common.model.event.order.OrderEventPayload;
import ru.shaplov.common.model.event.order.OrderEventType;
import ru.shaplov.common.outbox.EventPublisher;
import ru.shaplov.orderservice.model.ErrorInfo;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.OrderStatus;
import ru.shaplov.orderservice.service.OrderService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
public class OrderStatusEventHandler implements EventHandler<OrderEventPayload> {

    private final EventPublisher eventPublisher;
    private final OrderService orderService;
    private final Map<OrderEventType, OrderStatus> statusMap = Map.of(
            OrderEventType.PRODUCTS_RESERVATION_CANCELLED, OrderStatus.CANCELLED,
            OrderEventType.PRODUCTS_RESERVED, OrderStatus.RESERVED,
            OrderEventType.PAYMENT_PROCESSED, OrderStatus.PAYED,
            OrderEventType.IN_DELIVERY, OrderStatus.IN_DELIVERY,
            OrderEventType.FINISHED, OrderStatus.FINISHED
    );
    private final Set<OrderStatus> statusesToSend =
            Set.of(OrderStatus.CREATED, OrderStatus.FINISHED, OrderStatus.CANCELLED);

    public OrderStatusEventHandler(EventPublisher eventPublisher,
                                   OrderService orderService) {

        this.eventPublisher = eventPublisher;
        this.orderService = orderService;
    }

    @Override
    public List<String> eventTypes() {
        return List.of(OrderEventType.PRODUCTS_RESERVATION_CANCELLED.name(),
                OrderEventType.PRODUCTS_RESERVED.name(),
                OrderEventType.PAYMENT_PROCESSED.name(),
                OrderEventType.IN_DELIVERY.name(),
                OrderEventType.FINISHED.name());
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void processEvent(String key, OrderEventPayload payload, String orderEventType) {
        UUID orderId = UUID.fromString(key);
        ErrorDto error = payload.getError();

        Order order;
        if (OrderEventType.valueOf(orderEventType) == OrderEventType.PRODUCTS_RESERVATION_CANCELLED) {
            ErrorInfo errorInfo = new ErrorInfo()
                    .setMessage(error.getMessage())
                    .setReason(error.getReason());
            order = orderService.cancelOrder(orderId, errorInfo);
        } else {
            order = orderService.updateOrderStatus(orderId, statusMap.get(OrderEventType.valueOf(orderEventType)));
        }

        if (statusesToSend.contains(order.getOrderStatus())) {
            eventPublisher.publishEvent(NotificationEvent.of(new NotificationPayload()
                    .setEmail(order.getEmail())
                    .setOrderId(orderId)
                    .setErrorDto(payload.getError())
                    .setUserId(order.getUserId())
                    .setOrderStatus(order.getOrderStatus().name())
                    .setShippingAddress(order.getShippingAddress())
                    .setDeliveryDate(order.getDeliveryDate())));
        }
    }
}
