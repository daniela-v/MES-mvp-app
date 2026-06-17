package com.mvp.backend.application.service;

import com.mvp.backend.domain.Order;
import com.mvp.backend.domain.Payment;
import com.mvp.backend.domain.PaymentStatus;
import com.mvp.backend.infrastructure.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createSucceededMockPayment(Order order, Instant paidAt) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getCourse().getPrice());
        payment.setCurrency("GBP");
        payment.setProvider("MOCK_STRIPE");
        payment.setProviderPaymentId("pay_mock_" + UUID.randomUUID());
        payment.setStatus(PaymentStatus.SUCCEEDED);
        payment.setCreatedAt(paidAt);
        payment.setPaidAt(paidAt);
        return paymentRepository.save(payment);
    }
}
