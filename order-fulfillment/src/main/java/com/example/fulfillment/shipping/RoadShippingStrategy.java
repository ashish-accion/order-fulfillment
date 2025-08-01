package com.example.fulfillment.shipping;

import com.example.fulfillment.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class RoadShippingStrategy implements ShippingStrategy {
    @Override
    public void ship(Order order) {
        System.out.println("Shipping by ROAD: order " + order.getOrderId());
    }
}