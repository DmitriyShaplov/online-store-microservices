package com.example.deliveryservice.service;

import ru.shaplov.common.model.event.order.OrderDto;

import java.util.UUID;

public interface CourierService {
    void finishDelivery(UUID orderId);
    OrderDto attemptToReserveDeliverySlot(OrderDto orderDto);
}
