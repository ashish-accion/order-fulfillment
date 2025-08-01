package com.example.fulfillment.service;

import com.example.fulfillment.entity.Order;
import com.example.fulfillment.entity.OrderStatus;
import com.example.fulfillment.repo.OrderRepository;
import com.example.fulfillment.repo.OrderStatusLogRepository;
import com.example.fulfillment.shipping.ShippingStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@Service
public class FulfillmentProcessor {
    private final OrderRepository orderRepo;
    private final OrderStatusLogRepository logRepo;
    private final ShippingStrategyFactory factory;
    private final Executor taskExecutor;

    @Autowired
    public FulfillmentProcessor(OrderRepository orderRepo,
                                OrderStatusLogRepository logRepo,
                                ShippingStrategyFactory factory,
                                @Qualifier("taskExecutor") Executor taskExecutor) {
        this.orderRepo = orderRepo;
        this.logRepo = logRepo;
        this.factory = factory;
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedRateString = "${fulfillment.interval}")
    public void process() {
        log.info("Processing orders...");
        orderRepo.findByStatus(OrderStatus.PROCESSED)
                 .forEach(order -> CompletableFuture.runAsync(() -> handleOrder(order), taskExecutor));
    }

    @Transactional
    public void handleOrder(Order order) {
        try {
            log.info("Shipping order {}...", order.getOrderId());
            logRepo.save(new OrderStatusLog(order.getOrderId(), order.getStatus().name(), OrderStatus.SHIPPED.name()));
            factory.getStrategy(order.getShippingType()).ship(order);
            order.setStatus(OrderStatus.SHIPPED);
            order.setShippedAt(LocalDateTime.now());
            orderRepo.save(order);
            log.info("Order {} shipped.", order.getOrderId());
        } catch (ObjectOptimisticLockingFailureException ex) {
            log.error("Lock error shipping order {}.", order.getOrderId(), ex);
        }
    }
}