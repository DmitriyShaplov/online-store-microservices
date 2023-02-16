package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.shaplov.orderservice.model.OrderStatus;
import ru.shaplov.orderservice.model.PaymentMethod;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "orders", schema = "app_orders", catalog = "postgres")
public class OrderEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Version
    private int version;

    @Column(name = "profile_id")
    private Long profileId;

    @Column(name = "create_date", updatable = false)
    private OffsetDateTime createDate;

    @Column(name = "update_date")
    private OffsetDateTime updateDate;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "product_id")
    private UUID productId;

    private Integer quantity;

    @Column(name = "current_item_price")
    private BigDecimal currentItemPrice;

    @Column(name = "payment_method")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OrderEntity that = (OrderEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
