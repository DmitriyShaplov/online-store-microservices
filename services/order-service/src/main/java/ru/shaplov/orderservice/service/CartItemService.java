package ru.shaplov.orderservice.service;

import ru.shaplov.orderservice.model.CartItem;

import java.util.List;

public interface CartItemService {

    CartItem createOrUpdate(CartItem cartItem);

    List<CartItem> getList(Long userId);

    void clearCart(Long userId);
}
