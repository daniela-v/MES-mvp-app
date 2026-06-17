package com.mvp.backend.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CompleteOnboardingRequest(
        @NotBlank String token,
        @NotBlank String name,
        @NotBlank @Email String email,
        String password
) {
}
