package com.mvp.backend.application.service;

import com.mvp.backend.api.dto.CompleteOnboardingRequest;
import com.mvp.backend.api.dto.CompleteOnboardingResponse;
import com.mvp.backend.api.dto.OnboardingInfoResponse;
import com.mvp.backend.domain.Enrolment;
import com.mvp.backend.domain.EnrolmentStatus;
import com.mvp.backend.domain.Onboarding;
import com.mvp.backend.domain.Student;
import com.mvp.backend.domain.StudentStatus;
import com.mvp.backend.infrastructure.repository.EnrolmentRepository;
import com.mvp.backend.infrastructure.repository.OnboardingRepository;
import com.mvp.backend.infrastructure.repository.OrderRepository;
import com.mvp.backend.infrastructure.repository.StudentRepository;
import com.mvp.backend.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class OnboardingService {
    private final OnboardingRepository onboardingRepository;
    private final StudentRepository studentRepository;
    private final EnrolmentRepository enrolmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public OnboardingService(OnboardingRepository onboardingRepository, StudentRepository studentRepository,
                             EnrolmentRepository enrolmentRepository,
                             PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.onboardingRepository = onboardingRepository;
        this.studentRepository = studentRepository;
        this.enrolmentRepository = enrolmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Onboarding createAccessLink(Student student, Instant createdAt) {
        Onboarding onboarding = new Onboarding();
        onboarding.setStudent(student);
        onboarding.setToken(UUID.randomUUID().toString());
        onboarding.setExpiresAt(createdAt.plus(7, ChronoUnit.DAYS));
        return onboardingRepository.save(onboarding);
    }

    @Transactional(readOnly = true)
    public OnboardingInfoResponse getByToken(String token) {
        Onboarding onboarding = findValidToken(token, false);
        Student student = onboarding.getStudent();
        Enrolment enrolment = enrolmentRepository
                .findTopByStudentIdOrderByCreatedAtDesc(student.getId())
                .orElseThrow(() -> new IllegalArgumentException("Enrolment not found"));
        return new OnboardingInfoResponse(
                student.getId(),
                student.getName(),
                student.getEmail(),
                enrolment.getCourse().getSubject(),
                onboarding.getExpiresAt(),
                student.getStatus() != StudentStatus.ACTIVE
        );
    }

    @Transactional
    public CompleteOnboardingResponse complete(CompleteOnboardingRequest request) {
        Onboarding onboarding = findValidToken(request.token(), true);
        Student student = onboarding.getStudent();

        if (student.getStatus() != StudentStatus.ACTIVE) {
            if (!StringUtils.hasText(request.password())) {
                throw new IllegalArgumentException("Password is required"); //This validation shouldn't live here in prod app. this is mvp hack. onboarding should be re-written
            }
            student.setName(request.name());
            student.setEmail(request.email());
            student.setPasswordHash(passwordEncoder.encode(request.password()));
            student.setStatus(StudentStatus.ACTIVE);
            studentRepository.save(student);
        }

        enrolmentRepository.findByStudentIdAndStatus(student.getId(), EnrolmentStatus.PENDING)
                .forEach(enrolment -> enrolment.setStatus(EnrolmentStatus.ACTIVE));

        onboarding.setCompletedAt(Instant.now());
        onboardingRepository.save(onboarding);

        return new CompleteOnboardingResponse(student.getId(), student.getStatus().name(), jwtService.createToken(student));
    }

    private Onboarding findValidToken(String token, boolean rejectCompleted) {
        Onboarding onboarding = onboardingRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid onboarding token"));
        if (onboarding.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Invalid onboarding token");
        }
        if (rejectCompleted && onboarding.getCompletedAt() != null) {
            throw new IllegalArgumentException("Invalid onboarding token");
        }
        return onboarding;
    }
}
