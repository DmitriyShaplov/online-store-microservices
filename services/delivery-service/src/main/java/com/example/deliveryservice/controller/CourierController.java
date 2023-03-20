package com.example.deliveryservice.controller;

import com.example.deliveryservice.service.CourierService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CourierController {

    private final CourierService courierService;

    public CourierController(CourierService courierService) {
        this.courierService = courierService;
    }

    @PostMapping("/api/v1/delivery/{orderId}/finish")
    public void deliveryFinished(@PathVariable UUID orderId) {
        courierService.finishDelivery(orderId);
    }
}
