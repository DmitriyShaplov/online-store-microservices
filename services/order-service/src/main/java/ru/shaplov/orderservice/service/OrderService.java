package ru.shaplov.orderservice.service;

import ru.shaplov.orderservice.model.ErrorInfo;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.OrderStatus;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order get(UUID id);

    List<Order> findAll(Long userId);

    Order create(Order order);

    Order cancelOrder(UUID orderId, ErrorInfo errorInfo);

    Order updateOrderStatus(UUID orderId, OrderStatus status);
}
