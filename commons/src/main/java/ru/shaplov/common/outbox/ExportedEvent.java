package ru.shaplov.common.outbox;

public interface ExportedEvent<T> {

    String getAggregateId();

    String getAggregateType();

    T getPayload();

    String getType();
}