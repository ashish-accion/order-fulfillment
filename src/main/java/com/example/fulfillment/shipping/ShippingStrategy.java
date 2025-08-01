package com.example.fulfillment.shipping;

import com.example.fulfillment.entity.Order;

public interface ShippingStrategy {
    void ship(Order order);
}