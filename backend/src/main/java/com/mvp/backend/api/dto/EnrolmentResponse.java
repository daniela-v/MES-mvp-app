package com.mvp.backend.api.dto;

import java.time.Instant;

public record EnrolmentResponse(Long id, Long courseId, String courseSubject, String status, Instant createdAt, Instant validUntil) {
}
