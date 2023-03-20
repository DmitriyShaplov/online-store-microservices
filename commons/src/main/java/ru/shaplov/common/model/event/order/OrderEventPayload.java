package ru.shaplov.common.model.event.order;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import ru.shaplov.common.model.event.ErrorDto;

@Getter
@Builder
@JsonDeserialize(builder = OrderEventPayload.OrderEventPayloadBuilder.class)
public class OrderEventPayload {
    @Nullable
    private final OrderDto orderData;
    @Nullable
    private final ErrorDto error;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class OrderEventPayloadBuilder {
    }

    private OrderEventPayload(OrderDto orderData, ErrorDto error) {
        this.orderData = orderData;
        this.error = error;
    }

    public static OrderEventPayload of(OrderDto orderDto) {
        return new OrderEventPayload(orderDto, null);
    }

    public static OrderEventPayload of(ErrorDto error) {
        return new OrderEventPayload(null, error);
    }
}
