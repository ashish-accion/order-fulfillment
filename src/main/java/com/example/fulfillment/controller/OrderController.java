package com.example.fulfillment.controller;

import com.example.fulfillment.entity.Order;
import com.example.fulfillment.controller.dto.CreateOrderDto;
import com.example.fulfillment.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> placeOrder(@RequestBody CreateOrderDto dto) {
        log.info("Creating order for customer {}.", dto.getCustomerId());
        Order saved = orderService.createAndRouteOrder(dto.getCustomerId(), dto.getShippingType());
        return ResponseEntity.ok(saved);
    }
}