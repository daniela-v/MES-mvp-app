package com.mvp.backend.api.onboarding;

import com.mvp.backend.api.dto.CompleteOnboardingRequest;
import com.mvp.backend.api.dto.CompleteOnboardingResponse;
import com.mvp.backend.api.dto.OnboardingInfoResponse;
import com.mvp.backend.application.service.OnboardingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/onboarding")
public class OnboardingController {
    private final OnboardingService onboardingService;

    public OnboardingController(OnboardingService onboardingService) {
        this.onboardingService = onboardingService;
    }

    @GetMapping
    public OnboardingInfoResponse getOnboarding(@RequestParam String token) {
        return onboardingService.getByToken(token);
    }

    @PostMapping
    public CompleteOnboardingResponse completeOnboarding(@Valid @RequestBody CompleteOnboardingRequest request) {
        return onboardingService.complete(request);
    }
}
