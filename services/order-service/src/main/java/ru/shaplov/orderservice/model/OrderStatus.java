package ru.shaplov.orderservice.model;

import lombok.Getter;

public enum OrderStatus {
    CREATED(1),
    RESERVED(2),
    PAYED(3),
    IN_DELIVERY(4),
    FINISHED(5),

    CANCELLED(0);

    @Getter
    private final int state;

    OrderStatus(int state) {
        this.state = state;
    }
}
