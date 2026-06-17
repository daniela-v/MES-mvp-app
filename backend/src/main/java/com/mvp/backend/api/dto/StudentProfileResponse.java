package com.mvp.backend.api.dto;

public record StudentProfileResponse(Long id, String name, String email, String status, String dashboardName) {
}
