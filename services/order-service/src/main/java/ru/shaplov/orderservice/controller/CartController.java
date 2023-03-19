package ru.shaplov.orderservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.shaplov.common.util.SecurityUtil;
import ru.shaplov.orderservice.model.CartItem;
import ru.shaplov.orderservice.service.CartItemService;

import java.util.List;

@RestController
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PatchMapping("/api/v1/cart")
    public CartItem addCartItem(@RequestBody CartItem cartItem) {
        cartItem.setUserId(SecurityUtil.getCurrentUserId());
        return cartItemService.createOrUpdate(cartItem);
    }

    @GetMapping("/api/v1/cart")
    public List<CartItem> getCartItems() {
        return cartItemService.getList(SecurityUtil.getCurrentUserId());
    }
}
