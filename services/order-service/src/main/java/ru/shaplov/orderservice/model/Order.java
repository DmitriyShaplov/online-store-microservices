package ru.shaplov.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class Order {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @JsonIgnore
    private Long userId;

    @Min(10)
    @Max(20)
    private Integer deliveryHour;

    @NotNull
    private String shippingAddress;

    private List<OrderProduct> products;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus orderStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updateDate;
}
