package ru.shaplov.orderservice.service;

import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.PaymentMethod;

import java.util.UUID;

public interface OrderService {

    Order get(UUID id);

    Order create(Order order);

    Order update(UUID id, Order order);

    Order process(UUID id, PaymentMethod paymentMethod, int orderHash);

    Order cancel(UUID id, int orderHash);
}
