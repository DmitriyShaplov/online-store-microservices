package ru.shaplov.common.model.event.order;

public enum OrderEventType {
    /**
     * PROCESS EVENTS
     */
    ORDER_CREATED,
    PRODUCTS_RESERVED,
    PAYMENT_PROCESSED,
    IN_DELIVERY,
    FINISHED,

    /**
     * ERROR EVENTS
     */
    PRODUCTS_RESERVATION_CANCELLED,
    PAYMENT_CANCELLED,
    COURIER_ASSIGNMENT_CANCELLED
}
