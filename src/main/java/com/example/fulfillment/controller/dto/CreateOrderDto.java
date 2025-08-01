package com.example.fulfillment.controller.dto;

import com.example.fulfillment.entity.ShippingType;
import lombok.Data;

@Data
public class CreateOrderDto {
    private Long customerId;
    private ShippingType shippingType;
}