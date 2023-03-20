package ru.shaplov.orderservice.service.impl;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shaplov.orderservice.model.CartItem;
import ru.shaplov.orderservice.model.persistence.CartItemEntity;
import ru.shaplov.orderservice.repository.CartItemRepository;
import ru.shaplov.orderservice.service.CartItemService;

import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final JdbcTemplate jdbcTemplate;
    private final ModelMapper modelMapper;

    public CartItemServiceImpl(CartItemRepository cartItemRepository,
                               JdbcTemplate jdbcTemplate,
                               ModelMapper modelMapper) {
        this.cartItemRepository = cartItemRepository;
        this.jdbcTemplate = jdbcTemplate;
        this.modelMapper = modelMapper;
    }

    @Override
    public CartItem createOrUpdate(CartItem cartItem) {
        CartItemEntity cartItemEntity = modelMapper.map(cartItem, CartItemEntity.class);
        CartItemEntity updatedEntity = cartItemRepository.update(cartItemEntity, jdbcTemplate);
        return modelMapper.map(updatedEntity, CartItem.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getList(Long userId) {
        List<CartItemEntity> entityList = cartItemRepository.findByUserId(userId);
        return modelMapper.map(entityList, new TypeToken<List<CartItem>>() {
        }.getType());
    }

    @Override
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
