package ru.shaplovdv.product.service;

import ru.shaplov.common.model.event.order.OrderDto;

import java.util.UUID;

public interface ProductsOrderService {

    void processOrderReservation(OrderDto orderDto);

    void finishOrderProcessing(UUID orderId);

    void processOrderCancellation(UUID orderId);
}
