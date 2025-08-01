package com.example.fulfillment.shipping;

import com.example.fulfillment.entity.ShippingType;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class ShippingStrategyFactory {
    private final Map<ShippingType, ShippingStrategy> strategies;

    public ShippingStrategyFactory(RoadShippingStrategy road,
                                   AirShippingStrategy air) {
        strategies = Map.of(
            ShippingType.STANDARD, road,
            ShippingType.EXPRESS, air
        );
    }

    public ShippingStrategy getStrategy(ShippingType type) {
        return Optional.ofNullable(strategies.get(type))
            .orElseThrow(() -> new IllegalArgumentException("No strategy for " + type));
    }
}