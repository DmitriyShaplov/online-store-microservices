package ru.shaplov.common.consumer;

import java.util.List;

public interface EventHandler<T> {

    void processEvent(String key,
                      T payload,
                      String orderEventType);

    default void handleError(Exception e,
                             String key,
                             T payload,
                             String orderEventType) {
        throw new UnsupportedOperationException();
    }

    default boolean hasErrorProcessing() {
        return false;
    }

    List<String> eventTypes();
}
