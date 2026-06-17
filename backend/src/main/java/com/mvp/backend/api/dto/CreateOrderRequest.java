package com.mvp.backend.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderRequest(
        @NotBlank String parentName,
        @NotBlank @Email String parentEmail,
        @NotNull Long courseId,
        @NotBlank String studentName,
        @NotBlank @Email String studentEmail,
        @NotBlank String checkoutId
) {
}
