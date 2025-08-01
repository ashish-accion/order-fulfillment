package com.example.fulfillment.repo;

import com.example.fulfillment.entity.OrderStatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderStatusLogRepository extends JpaRepository<OrderStatusLog, Long> {
    List<OrderStatusLog> findByNewStatusInAndNotifiedFalse(List<String> statuses);
}