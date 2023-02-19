package ru.shaplov.orderservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Order {

    private UUID id;

    @JsonIgnore
    private Integer version;

    @JsonIgnore
    private Long profileId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime createDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OffsetDateTime updateDate;

    @NotNull
    private String shippingAddress;

    @NotNull
    private UUID productId;

    @NotNull
    private Integer quantity;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal currentItemPrice;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private PaymentMethod paymentMethod;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private OrderStatus orderStatus;

    private int orderHash;

    @JsonProperty("totalPrice")
    public BigDecimal getTotalPrice() {
        return currentItemPrice.multiply(BigDecimal.valueOf(quantity));
    }

    @JsonProperty("orderHash")
    public int getOuterOrderHash() {
        return hashCode();
    }

    @JsonProperty("orderHash")
    public void setOrderHash(int orderHash) {
        this.orderHash = orderHash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(profileId, order.profileId) && Objects.equals(createDate, order.createDate) && Objects.equals(updateDate, order.updateDate) && Objects.equals(shippingAddress, order.shippingAddress) && Objects.equals(productId, order.productId) && Objects.equals(quantity, order.quantity) && Objects.equals(currentItemPrice, order.currentItemPrice) && paymentMethod == order.paymentMethod && orderStatus == order.orderStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, profileId, createDate, updateDate, shippingAddress, productId, quantity, currentItemPrice, paymentMethod, orderStatus);
    }
}
