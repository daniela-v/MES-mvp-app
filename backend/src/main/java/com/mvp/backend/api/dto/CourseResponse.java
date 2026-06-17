package com.mvp.backend.api.dto;

import com.mvp.backend.domain.SchoolYear;

import java.math.BigDecimal;

public record CourseResponse(Long id, String subject, SchoolYear schoolYear, BigDecimal price) {
}
