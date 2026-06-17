package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.CreateOrderRequest;
import com.mvp.backend.api.dto.CreateOrderResponse;
import com.mvp.backend.domain.Course;
import com.mvp.backend.domain.Onboarding;
import com.mvp.backend.domain.Order;
import com.mvp.backend.domain.OrderStatus;
import com.mvp.backend.domain.Student;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final CourseService courseService;
    private final PaymentService paymentService;
    private final StudentService studentService;
    private final EnrolmentService enrolmentService;
    private final OnboardingService onboardingService;

    public OrderService(OrderRepository orderRepository,
                        CourseService courseService,
                        PaymentService paymentService,
                        StudentService studentService,
                        EnrolmentService enrolmentService,
                        OnboardingService onboardingService) {
        this.orderRepository = orderRepository;
        this.courseService = courseService;
        this.paymentService = paymentService;
        this.studentService = studentService;
        this.enrolmentService = enrolmentService;
        this.onboardingService = onboardingService;
    }

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        return orderRepository.findByCheckoutId(request.checkoutId())
                .map(this::toExistingOrderResponse)
                .orElseGet(() -> createPaidOrder(request));
    }

    private CreateOrderResponse createPaidOrder(CreateOrderRequest request) {
        Instant now = Instant.now();
        Course course = courseService.getCourseEntity(request.courseId());

        Order order = new Order();
        order.setParentName(request.parentName());
        order.setParentEmail(request.parentEmail());
        order.setCourse(course);
        order.setStudentName(request.studentName());
        order.setStudentEmail(request.studentEmail());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setCreatedAt(now);
        order.setCheckoutId(request.checkoutId());

        Order savedOrder = orderRepository.save(order);

        paymentService.createSucceededMockPayment(savedOrder, now); //Mocking. Should be entirely decoupled and re-written to be async.
        savedOrder.setStatus(OrderStatus.PAID);
        orderRepository.save(savedOrder);

        Student student = studentService.findOrCreateInvitedStudent(
                savedOrder.getStudentName(),
                savedOrder.getStudentEmail()
        );
        enrolmentService.createPendingEnrolment(
                student,
                savedOrder.getCourse(),
                savedOrder,
                now
        );

        Onboarding access = onboardingService.createAccessLink(student, now);
        return new CreateOrderResponse(
                savedOrder.getId(),
                savedOrder.getStatus(),
                access.getStudent().getStatus() != StudentStatus.ACTIVE,
                onboardingUrl(access)
        );
    }

    private CreateOrderResponse toExistingOrderResponse(Order order) {
        Student student = studentService.findOrCreateInvitedStudent(order.getStudentName(), order.getStudentEmail());
        Onboarding access = onboardingService.createAccessLink(student, Instant.now());
        return new CreateOrderResponse(
                order.getId(),
                order.getStatus(),
                access.getStudent().getStatus() != StudentStatus.ACTIVE,
                onboardingUrl(access)
        );
    }

    private String onboardingUrl(Onboarding onboarding) {
        return "/onboarding?token=" + onboarding.getToken();
    }
}
