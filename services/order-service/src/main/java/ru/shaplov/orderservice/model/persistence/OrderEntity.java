package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import ru.shaplov.orderservice.model.OrderStatus;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "orders", schema = "os_orders", catalog = "postgres")
public class OrderEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "create_date", updatable = false)
    private OffsetDateTime createDate;

    @Column(name = "update_date")
    private OffsetDateTime updateDate;

    @Column(name = "delivery_hour")
    private Integer deliveryHour;

    @Column(name = "shipping_address")
    private String shippingAddress;

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
