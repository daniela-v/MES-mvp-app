package com.mvp.backend.api.dto;

import com.mvp.backend.domain.OrderStatus;

public record CreateOrderResponse(
        Long orderId,
        OrderStatus status,
        boolean onboardingRequired,
        String accessUrl
) {
}
