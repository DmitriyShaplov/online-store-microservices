package ru.shaplov.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Order {

    @NotNull
    private UUID id;

    @JsonIgnore
    private Long userId;

    @JsonIgnore
    private String email;

    @NotNull
    private OffsetDateTime deliveryDate;

    @NotNull
    private String shippingAddress;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String courierInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private ErrorInfo errorInfo;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<OrderProduct> products;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus orderStatus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updateDate;
}
