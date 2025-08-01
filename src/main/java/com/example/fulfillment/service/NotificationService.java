package com.example.fulfillment.service;

import com.example.fulfillment.entity.OrderStatusLog;
import com.example.fulfillment.repo.OrderStatusLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class NotificationService {
    private final OrderStatusLogRepository logRepo;
    public NotificationService(OrderStatusLogRepository logRepo) { this.logRepo = logRepo; }

    @Scheduled(fixedDelayString = "${notification.delay}")
    public void notifyCustomers() {
        log.info("Notifying customers...");
        List<OrderStatusLog> entries = logRepo.findByNewStatusInAndNotifiedFalse(List.of("SHIPPED"));
        entries.forEach(e -> {
            log.info("Notify for order {}.", e.getOrderId());
            // Call to external notification service
            e.setNotified(true);
            logRepo.save(e);
        });
        log.info("Notifications sent.");
    }
}