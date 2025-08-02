package com.example.fulfillment.service;

import com.example.fulfillment.entity.Order;
import com.example.fulfillment.entity.OrderStatus;
import com.example.fulfillment.repo.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Slf4j
@Service
public class RoutingService {
    private final OrderRepository repo;
    public RoutingService(OrderRepository repo) { this.repo = repo; }

    @Transactional
    public void assignFulfillmentCenter(Long orderId, int fcId) {
        int maxRetries = 3;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Order order = repo.findById(orderId)
                                  .orElseThrow(() -> new EntityNotFoundException("Order not found: " + orderId));
                if (order.getStatus() != OrderStatus.OPEN) {
                    log.info("Skipping order {} (status {}).", orderId, order.getStatus());
                    return;
                }
                order.setAssignedFc(fcId);
                order.setStatus(OrderStatus.PROCESSED);
                repo.save(order);
                log.info("Order {} assigned to FC {}.", orderId, fcId);
                return;
            } catch (ObjectOptimisticLockingFailureException ex) {
                log.warn("Lock failure for order {} (attempt {}).", orderId, attempt);
                if (attempt == maxRetries) { throw ex; }
            }
        }
    }

    public void routeOrder(Order order) {
        assignFulfillmentCenter(order.getOrderId(), (int)((order.getOrderId()%150)+1));
    }

    @Scheduled(fixedRateString = "${routing.interval}")
    public void routePendingOrders() {
        log.info("Routing pending orders..."); repo.findByStatus(OrderStatus.OPEN).forEach(this::routeOrder);
    }
}