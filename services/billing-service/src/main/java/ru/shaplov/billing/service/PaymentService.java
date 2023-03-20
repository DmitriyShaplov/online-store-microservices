package ru.shaplov.billing.service;

import ru.shaplov.common.model.event.order.OrderDto;

import java.util.UUID;

public interface PaymentService {

    void processPayment(OrderDto order);

    void processPaymentCancellation(UUID orderId);
}
