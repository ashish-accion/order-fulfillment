package com.example.fulfillment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_status_log")
public class OrderStatusLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    private Long orderId;
    private String oldStatus;
    private String newStatus;
    private LocalDateTime changedAt = LocalDateTime.now();
    private boolean notified = false;

    public OrderStatusLog(Long orderId, String name, String name1) {
    }
}