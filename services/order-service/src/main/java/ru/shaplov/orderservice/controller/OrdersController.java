package ru.shaplov.orderservice.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrdersController {

    final List<SecurityFilterChain> chainList;

    public OrdersController(List<SecurityFilterChain> chainList) {
        this.chainList = chainList;
    }

    @GetMapping("/echo")
    public void echo(Authentication authentication) {
        System.out.println("echo");
    }
}
