package com.example.fulfillment.service;

import com.example.fulfillment.entity.Order;
import com.example.fulfillment.entity.ShippingType;
import com.example.fulfillment.entity.OrderStatus;
import com.example.fulfillment.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepo;
    private final RoutingService routingService;

    public OrderService(OrderRepository orderRepo, RoutingService routingService) {
        this.orderRepo = orderRepo;
        this.routingService = routingService;
    }

    @Transactional
    public Order createAndRouteOrder(Long customerId, ShippingType shippingType) {
        Order newOrder = Order.builder()
                              .customerId(customerId)
                              .shippingType(shippingType)
                              .status(OrderStatus.OPEN)
                              .build();
        Order saved = orderRepo.save(newOrder);
        log.info("Order {} created for customer {}.", saved.getOrderId(), customerId);
        routingService.routeOrder(saved);
        return saved;
    }
}