package ru.shaplov.orderservice.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.shaplov.common.util.SecurityUtil;
import ru.shaplov.orderservice.model.Order;
import ru.shaplov.orderservice.service.OrderService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/{id}")
    public Order get(@PathVariable UUID id) {
        return orderService.get(id);
    }

    @GetMapping
    public List<Order> getList() {
        return orderService.findAll(SecurityUtil.getCurrentUserId());
    }

    @PostMapping
    public Order create(@RequestBody @Valid Order order) {
        order.setUserId(SecurityUtil.getCurrentUserId());
        order.setEmail((String) SecurityUtil.getAttribute("email"));
        return orderService.create(order);
    }
}
