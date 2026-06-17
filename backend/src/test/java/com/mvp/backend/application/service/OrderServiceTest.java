package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.CreateOrderRequest;
import com.mvp.backend.domain.*;
import com.mvp.backend.infrastructure.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository orderRepository;
    @Mock CourseService courseService;
    @Mock PaymentService paymentService;
    @Mock StudentService studentService;
    @Mock EnrolmentService enrolmentService;
    @Mock OnboardingService onboardingService;
    @InjectMocks OrderService orderService;

    @Test
    void createOrderMocksPaymentMarksOrderPaidAndCreatesAccessWithOnboardingWhenRequired() {
        var course = TestData.course(1);
        var student = TestData.student(2, StudentStatus.INVITED);
        var enrolment = TestData.enrolment(5, student, course);
        var onboarding = TestData.onboarding(4, student);
        var payment = succeededPayment("pay_mock_123");

        when(courseService.getCourseEntity(1L)).thenReturn(course);
        when(orderRepository.save(any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) {
                ReflectionTestUtils.setField(order, "id", 3L);
            }
            return order;
        });
        when(paymentService.createSucceededMockPayment(any(Order.class), any(Instant.class))).thenReturn(payment);
        when(studentService.findOrCreateInvitedStudent("Ada", "student@example.com")).thenReturn(student);
        when(enrolmentService.createPendingEnrolment(eq(student), eq(course), any(Order.class), any(Instant.class))).thenReturn(enrolment);
        when(onboardingService.createAccessLink(eq(student), any(Instant.class))).thenReturn(onboarding);

        var response = orderService.createOrder(new CreateOrderRequest(
                "Parent", "parent@example.com", 1L, "Ada", "student@example.com", "checkout-test"));

        assertThat(response.orderId()).isEqualTo(3L);
        assertThat(response.status()).isEqualTo(OrderStatus.PAID);
                assertThat(response.onboardingRequired()).isTrue();
        assertThat(response.accessUrl()).isEqualTo("/onboarding?token=token-123");

        verify(orderRepository, org.mockito.Mockito.times(2)).save(any(Order.class));
        assertThat(response.status()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void createOrderReturnsAccessUrlWhenOnboardingIsNotRequired() {
        var course = TestData.course(1);
        var student = TestData.student(2, StudentStatus.ACTIVE);
        var enrolment = TestData.enrolment(5, student, course);
        var payment = succeededPayment("pay_mock_456");

        when(courseService.getCourseEntity(1L)).thenReturn(course);
        when(orderRepository.save(any())).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            if (order.getId() == null) {
                ReflectionTestUtils.setField(order, "id", 3L);
            }
            return order;
        });
        when(paymentService.createSucceededMockPayment(any(Order.class), any(Instant.class))).thenReturn(payment);
        when(studentService.findOrCreateInvitedStudent("Ada", "student@example.com")).thenReturn(student);
        when(enrolmentService.createPendingEnrolment(eq(student), eq(course), any(Order.class), any(Instant.class))).thenReturn(enrolment);
        when(onboardingService.createAccessLink(eq(student), any(Instant.class))).thenReturn(TestData.onboarding(4, student));

        var response = orderService.createOrder(new CreateOrderRequest(
                "Parent", "parent@example.com", 1L, "Ada", "student@example.com", "checkout-test"));

        assertThat(response.status()).isEqualTo(OrderStatus.PAID);
        assertThat(response.onboardingRequired()).isFalse();
        assertThat(response.accessUrl()).isEqualTo("/onboarding?token=token-123");
    }

    private Payment succeededPayment(String providerPaymentId) {
        Payment payment = new Payment();
        payment.setProviderPaymentId(providerPaymentId);
        payment.setStatus(PaymentStatus.SUCCEEDED);
        return payment;
    }
}
