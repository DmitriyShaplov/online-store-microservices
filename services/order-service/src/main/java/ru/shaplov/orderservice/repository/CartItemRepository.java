package ru.shaplov.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.shaplov.orderservice.model.persistence.CartItemEntity;
import ru.shaplov.orderservice.model.persistence.CartItemId;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemId> {

    List<CartItemEntity> findByUserId(Long accountId);

    void deleteByUserId(Long userId);

    default CartItemEntity update(CartItemEntity cartItem, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.queryForObject("""
                        INSERT INTO cart_items (user_id, product_id, quantity)
                                    VALUES (?, ?, ?)
                                    ON CONFLICT (user_id, product_id)
                                    DO UPDATE SET quantity = cart_items.quantity + ?
                                    RETURNING *
                        """, (rs, rowNum) -> new CartItemEntity()
                        .setUserId(rs.getLong(1))
                        .setProductId((UUID) rs.getObject(2))
                        .setQuantity(rs.getInt(3)),
                cartItem.getUserId(), cartItem.getProductId(), cartItem.getQuantity(), cartItem.getQuantity()
        );
    }
}
