package com.mvp.backend.infrastructure.repository;

import com.mvp.backend.domain.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    Optional<Onboarding> findByToken(String token);
}
