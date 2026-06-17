package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.CompleteOnboardingRequest;
import com.mvp.backend.domain.EnrolmentStatus;
import com.mvp.backend.domain.OrderStatus;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.EnrolmentRepository;
import com.mvp.backend.infrastructure.repository.OnboardingRepository;
import com.mvp.backend.infrastructure.repository.OrderRepository;
import com.mvp.backend.infrastructure.repository.StudentRepository;
import com.mvp.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OnboardingServiceTest {
    @Mock OnboardingRepository onboardingRepository;
    @Mock StudentRepository studentRepository;
    @Mock EnrolmentRepository enrolmentRepository;
    @Mock OrderRepository orderRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtService jwtService;
    @InjectMocks OnboardingService onboardingService;


    @Test
    void createAccessLinkCreatesTokenForInvitedStudent() {
        var student = TestData.student(2, StudentStatus.INVITED);
        var onboarding = TestData.onboarding(4, student);
        when(onboardingRepository.save(any())).thenReturn(onboarding);

        var result = onboardingService.createAccessLink(student, Instant.parse("2026-06-16T00:00:00Z"));

        assertThat(result).isEqualTo(onboarding);
        verify(onboardingRepository).save(any());
    }

    @Test
    void createAccessLinkCreatesTokenForActiveStudent() {
        var student = TestData.student(2, StudentStatus.ACTIVE);
        var onboarding = TestData.onboarding(4, student);
        when(onboardingRepository.save(any())).thenReturn(onboarding);

        var result = onboardingService.createAccessLink(student, Instant.parse("2026-06-16T00:00:00Z"));

        assertThat(result).isEqualTo(onboarding);
        verify(onboardingRepository).save(any());
    }

    @Test
    void getByTokenReturnsPendingInvitationDetails() {
        var student = TestData.student(2, StudentStatus.INVITED);
        var enrolment = TestData.enrolment(3, student, TestData.course(1));
        enrolment.getOrder().setStatus(OrderStatus.PAID);
        when(onboardingRepository.findByToken("token-123")).thenReturn(Optional.of(TestData.onboarding(4, student)));
        when(enrolmentRepository.findTopByStudentIdOrderByCreatedAtDesc(2L)).thenReturn(Optional.of(enrolment));

        var response = onboardingService.getByToken("token-123");

        assertThat(response.studentName()).isEqualTo("Ada");
        assertThat(response.courseSubject()).isEqualTo("Maths");
    }

    @Test
    void completeActivatesStudentAndPendingEnrolments() {
        var student = TestData.student(2, StudentStatus.INVITED);
        var onboarding = TestData.onboarding(4, student);
        var enrolment = TestData.enrolment(3, student, TestData.course(1));
        enrolment.getOrder().setStatus(OrderStatus.PAID);
        when(onboardingRepository.findByToken("token-123")).thenReturn(Optional.of(onboarding));
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(enrolmentRepository.findByStudentIdAndStatus(2L, EnrolmentStatus.PENDING)).thenReturn(List.of(enrolment));
        when(jwtService.createToken(student)).thenReturn("jwt");

        var response = onboardingService.complete(new CompleteOnboardingRequest("token-123", "Ada", "ada@example.com", "secret123"));

        assertThat(response.token()).isEqualTo("jwt");
        assertThat(student.getStatus()).isEqualTo(StudentStatus.ACTIVE);
        assertThat(student.getPasswordHash()).isEqualTo("hashed");
        assertThat(enrolment.getStatus()).isEqualTo(EnrolmentStatus.ACTIVE);
        assertThat(enrolment.getOrder().getStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(onboarding.getCompletedAt()).isNotNull();
    }

    @Test
    void completeRejectsExpiredToken() {
        var onboarding = TestData.onboarding(4, TestData.student(2, StudentStatus.INVITED));
        onboarding.setExpiresAt(Instant.now().minusSeconds(1));
        when(onboardingRepository.findByToken("token-123")).thenReturn(Optional.of(onboarding));

        assertThatThrownBy(() -> onboardingService.complete(new CompleteOnboardingRequest("token-123", "Ada", "ada@example.com", "secret123")))
                .hasMessage("Invalid onboarding token");
    }
}
