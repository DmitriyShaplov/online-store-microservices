package ru.shaplov.common.model.event.order;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDto {

    private UUID id;

    private Long userId;

    private String email;

    private OffsetDateTime deliveryDate;

    private String shippingAddress;

    private String courierInfo;

    private List<OrderProduct> products;

    private OffsetDateTime createDate;
}
