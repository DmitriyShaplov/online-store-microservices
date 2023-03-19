package ru.shaplov.orderservice.service.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.orderservice.model.CartItem;
import ru.shaplov.orderservice.model.persistence.CartItemEntity;
import ru.shaplov.orderservice.model.persistence.CartItemId;
import ru.shaplov.orderservice.repository.CartItemRepository;
import ru.shaplov.orderservice.service.CartItemService;

import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final JdbcTemplate jdbcTemplate;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               JdbcTemplate jdbcTemplate) {
        this.cartItemRepository = cartItemRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public CartItem createOrUpdate(CartItem cartItem) {
        CartItemEntity cartItemEntity = mapToEntity(cartItem);
        CartItemEntity updatedEntity = cartItemRepository.update(cartItemEntity, jdbcTemplate);
        return mapToCartItem(updatedEntity);
    }

    private CartItem mapToCartItem(CartItemEntity updatedEntity) {
        return new CartItem()
                .setQuantity(updatedEntity.getQuantity())
                .setUserId(updatedEntity.getCartItemId().getUserId())
                .setProductId(updatedEntity.getCartItemId().getProductId());
    }

    private CartItemEntity mapToEntity(CartItem cartItem) {
        return new CartItemEntity()
                .setQuantity(cartItem.getQuantity())
                .setCartItemId(new CartItemId().setProductId(cartItem.getProductId())
                        .setUserId(cartItem.getUserId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getList(Long accountId) {
        List<CartItemEntity> entityList = cartItemRepository.findByCartItemIdUserId(accountId);
        return entityList.stream()
                .map(this::mapToCartItem)
                .toList();
    }
}
