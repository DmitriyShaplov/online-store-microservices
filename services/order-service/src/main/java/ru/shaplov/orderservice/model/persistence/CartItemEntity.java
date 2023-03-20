package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "cart_items")
@IdClass(CartItemId.class)
public class CartItemEntity {

    @Id
    private Long userId;

    @Id
    private UUID productId;

    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CartItemEntity item = (CartItemEntity) o;
        return userId != null && Objects.equals(userId, item.userId)
                && productId != null && Objects.equals(productId, item.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, productId);
    }
}
