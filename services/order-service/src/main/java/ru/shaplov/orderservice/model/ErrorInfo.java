package ru.shaplov.orderservice.model;

import lombok.Data;

@Data
public class ErrorInfo {
    private String message;
    private String reason;
}
