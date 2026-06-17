package com.mvp.backend.application.service;

import com.mvp.backend.domain.Payment;
import com.mvp.backend.domain.PaymentStatus;
import com.mvp.backend.infrastructure.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock PaymentRepository paymentRepository;
    @InjectMocks PaymentService paymentService;

    @Test
    void createSucceededMockPaymentPersistsSucceededPaymentForOrder() {
        var course = TestData.course(1);
        var order = TestData.order(3, course);
        Instant paidAt = Instant.parse("2026-06-16T00:00:00Z");
        when(paymentRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var payment = paymentService.createSucceededMockPayment(order, paidAt);

        assertThat(payment.getProviderPaymentId()).startsWith("pay_mock_");
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.SUCCEEDED);
        assertThat(payment.getPaidAt()).isEqualTo(paidAt);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepository).save(paymentCaptor.capture());
        assertThat(paymentCaptor.getValue().getOrder()).isEqualTo(order);
        assertThat(paymentCaptor.getValue().getAmount()).isEqualByComparingTo(course.getPrice());
        assertThat(paymentCaptor.getValue().getProvider()).isEqualTo("MOCK_STRIPE");
    }
}
