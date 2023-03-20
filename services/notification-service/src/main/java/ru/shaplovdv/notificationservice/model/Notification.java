package ru.shaplovdv.notificationservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notification {

    private Long id;
    private String email;
    private String message;
    private Long userId;
    private UUID orderId;
    private OffsetDateTime date;
    private OffsetDateTime processedDate;
}
