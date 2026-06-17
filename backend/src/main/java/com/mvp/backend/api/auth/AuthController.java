package com.mvp.backend.api.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/logout")
    @ResponseStatus(NO_CONTENT)
    public void logout() {
        // JWT logout is client-side: discard the bearer token.
    }
}
