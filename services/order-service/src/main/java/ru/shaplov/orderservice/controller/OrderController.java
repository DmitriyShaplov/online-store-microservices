package ru.shaplov.orderservice.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.model.PaymentMethod;
import ru.shaplov.orderservice.service.OrderService;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable UUID id) {
        return orderService.get(id);
    }

    @PostMapping
    public Order create(@RequestBody Order order, @AuthenticationPrincipal Jwt jwt) {
        order.setProfileId(jwt.getClaim("profile_id"));
        return orderService.create(order);
    }

    @PutMapping("/{id}")
    public Order update(@PathVariable UUID id, @RequestBody Order order) {
        return orderService.update(id, order);
    }

    @PostMapping("/{id}")
    public Order processPayment(@PathVariable UUID id, @RequestParam PaymentMethod paymentMethod, @RequestParam int orderHash) {
        return orderService.process(id, paymentMethod, orderHash);
    }

    @DeleteMapping("/{id}")
    public void cancel(@PathVariable UUID id, @RequestParam int orderHash) {
        orderService.cancel(id, orderHash);
    }

}
