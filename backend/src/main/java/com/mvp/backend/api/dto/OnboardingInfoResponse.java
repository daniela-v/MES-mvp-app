package com.mvp.backend.api.dto;

import java.time.Instant;

public record OnboardingInfoResponse(
        Long studentId,
        String studentName,
        String studentEmail,
        String courseSubject,
        Instant expiresAt,
        boolean onboardingRequired
) {
}
