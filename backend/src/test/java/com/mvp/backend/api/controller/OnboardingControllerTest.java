package com.mvp.backend.api.controller;

import com.mvp.backend.api.dto.CompleteOnboardingRequest;
import com.mvp.backend.api.dto.CompleteOnboardingResponse;
import com.mvp.backend.api.dto.OnboardingInfoResponse;
import com.mvp.backend.api.onboarding.OnboardingController;
import com.mvp.backend.application.service.OnboardingService;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OnboardingControllerTest {
    @Test
    void getOnboardingDelegatesToService() {
        OnboardingService service = mock(OnboardingService.class);
        when(service.getByToken("abc")).thenReturn(new OnboardingInfoResponse(1L, "Ada", "ada@example.com", "Maths", Instant.now(), true));

        assertThat(new OnboardingController(service).getOnboarding("abc").courseSubject()).isEqualTo("Maths");
    }

    @Test
    void completeOnboardingDelegatesToService() {
        OnboardingService service = mock(OnboardingService.class);
        CompleteOnboardingRequest request = new CompleteOnboardingRequest("abc", "Ada", "ada@example.com", "secret123");
        when(service.complete(request)).thenReturn(new CompleteOnboardingResponse(1L, "ACTIVE", "jwt"));

        assertThat(new OnboardingController(service).completeOnboarding(request).token()).isEqualTo("jwt");
    }
}
