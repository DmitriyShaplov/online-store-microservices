package ru.shaplov.orderservice.service;

import ru.shaplov.orderservice.model.Order;

import java.util.UUID;

public interface OrderPersistentService {

    Order find(UUID id);

    boolean exists(UUID id);

    Order saveOrUpdate(Order order);
}
