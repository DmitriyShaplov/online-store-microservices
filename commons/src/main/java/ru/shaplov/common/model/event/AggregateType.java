package ru.shaplov.common.model.event;

import lombok.Getter;

public enum AggregateType {
    ORDER("order"),
    NOTIFICATION("notification")
    ;

    @Getter
    private final String value;

    AggregateType(String value) {
        this.value = value;
    }
}
