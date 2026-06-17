package com.mvp.backend.api.controller;

import com.mvp.backend.api.dto.CreateOrderRequest;
import com.mvp.backend.api.dto.CreateOrderResponse;
import com.mvp.backend.api.order.OrderController;
import com.mvp.backend.application.service.OrderService;
import com.mvp.backend.domain.OrderStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderControllerTest {
    @Test
    void createOrderDelegatesToService() {
        OrderService service = mock(OrderService.class);
        CreateOrderRequest request = new CreateOrderRequest("Parent", "parent@example.com", 1L, "Ada", "ada@example.com", "checkout-test");
        when(service.createOrder(request)).thenReturn(new CreateOrderResponse(
                1L, OrderStatus.PAID, true, "/onboarding?token=abc"));

        var response = new OrderController(service).createOrder(request);

        assertThat(response.status()).isEqualTo(OrderStatus.PAID);
        assertThat(response.onboardingRequired()).isTrue();
        assertThat(response.accessUrl()).isEqualTo("/onboarding?token=abc");
    }
}
