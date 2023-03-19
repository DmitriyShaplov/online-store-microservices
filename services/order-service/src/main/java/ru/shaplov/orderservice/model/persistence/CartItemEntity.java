package ru.shaplov.orderservice.model.persistence;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItemEntity {

    @EmbeddedId
    private CartItemId cartItemId;

    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CartItemEntity that = (CartItemEntity) o;
        return cartItemId != null && Objects.equals(cartItemId, that.cartItemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartItemId);
    }
}
