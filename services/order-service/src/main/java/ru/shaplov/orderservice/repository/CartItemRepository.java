package ru.shaplov.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import ru.shaplov.orderservice.model.persistence.CartItemEntity;
import ru.shaplov.orderservice.model.persistence.CartItemId;

import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItemEntity, CartItemId> {

    String UPDATE_QUERY = """
            INSERT INTO cart_items (user_id, product_id, quantity) 
            VALUES (?, ?, ?)
            ON CONFLICT (user_id, product_id)
            DO UPDATE SET quantity = cart_items.quantity + ?
            RETURNING *
            """;

    List<CartItemEntity> findByCartItemIdUserId(Long accountId);

    default CartItemEntity update(CartItemEntity cartItem, JdbcTemplate jdbcTemplate) {
        return jdbcTemplate.execute((PreparedStatementCreator) con -> con.prepareStatement(UPDATE_QUERY),
                preparedStatement -> {
                    preparedStatement.setLong(1, cartItem.getCartItemId().getUserId());
                    preparedStatement.setObject(2, cartItem.getCartItemId().getProductId());
                    preparedStatement.setInt(3, cartItem.getQuantity());
                    preparedStatement.setInt(4, cartItem.getQuantity());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    resultSet.next();
                    return new CartItemEntity()
                            .setCartItemId(new CartItemId()
                                    .setUserId(resultSet.getLong(1))
                                    .setProductId((UUID) resultSet.getObject(2)))
                            .setQuantity(resultSet.getInt(3));
                });
    }
}
