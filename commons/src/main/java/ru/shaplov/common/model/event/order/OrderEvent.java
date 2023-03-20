package ru.shaplov.common.model.event.order;

import ru.shaplov.common.model.event.AggregateType;
import ru.shaplov.common.model.event.ErrorDto;
import ru.shaplov.common.outbox.ExportedEvent;

import java.util.UUID;

public class OrderEvent implements ExportedEvent<OrderEventPayload> {

    private final String aggregateId;
    private final OrderEventPayload payload;
    private final OrderEventType eventType;

    private OrderEvent(String aggregateId, OrderEventPayload payload, OrderEventType eventType) {
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.eventType = eventType;
    }

    public static OrderEvent of(UUID orderId, OrderEventType eventType) {
        return new OrderEvent(orderId.toString(), OrderEventPayload.builder().build(), eventType);
    }

    public static OrderEvent of(OrderDto order, OrderEventType eventType) {
        return new OrderEvent(order.getId().toString(), OrderEventPayload.of(order), eventType);
    }

    public static OrderEvent of(UUID id, ErrorDto error, OrderEventType eventType) {
        return new OrderEvent(id.toString(), OrderEventPayload.of(error), eventType);
    }

    @Override
    public String getAggregateId() {
        return aggregateId;
    }

    @Override
    public String getAggregateType() {
        return AggregateType.ORDER.getValue();
    }

    @Override
    public OrderEventPayload getPayload() {
        return payload;
    }

    @Override
    public String getType() {
        return eventType.name();
    }
}
