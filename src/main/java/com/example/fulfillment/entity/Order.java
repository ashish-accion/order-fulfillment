package com.example.fulfillment.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Long customerId;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private ShippingType shippingType;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private OrderStatus status = OrderStatus.OPEN;

    private Integer assignedFc;
    private LocalDateTime shippedAt;
    private LocalDateTime createdAt = LocalDateTime.now();

    @Version
    private Long version;
}