package ru.shaplov.orderservice.model;

import lombok.Getter;

public enum OrderStatus {
    DRAFT(0),
    PROCESSED(1),
    SHIPPED(2),
    DELIVERED(3),
    CANCELLED(4);

    @Getter
    private final int state;

    OrderStatus(int state) {
        this.state = state;
    }
}
