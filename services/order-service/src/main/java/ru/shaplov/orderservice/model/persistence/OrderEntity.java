package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.shaplov.orderservice.model.ErrorInfo;
import ru.shaplov.orderservice.model.OrderStatus;

import java.time.OffsetDateTime;
import java.util.List;
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

    private String email;

    @Column(name = "create_date", updatable = false)
    private OffsetDateTime createDate;

    @Column(name = "update_date")
    private OffsetDateTime updateDate;

    @Column(name = "delivery_date")
    private OffsetDateTime deliveryDate;

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "courier_info")
    private String courierInfo;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "error_info")
    private ErrorInfo errorInfo;

    @ElementCollection
    @CollectionTable(name = "order_products", joinColumns = @JoinColumn(name = "order_id"))
    private List<OrderProductEntity> products;

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
