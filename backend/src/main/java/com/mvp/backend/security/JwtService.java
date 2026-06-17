package com.mvp.backend.security;

import com.mvp.backend.domain.Student;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey signingKey;
    private final long ttlSeconds;

    public JwtService(@Value("${security.jwt.secret:change-this-secret-in-production-change-this-secret}") String secret,
                      @Value("${security.jwt.ttl-seconds:604800}") long ttlSeconds) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
    }

    public String createToken(Student student) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(String.valueOf(student.getId()))
                .claim("email", student.getEmail())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(signingKey)
                .compact();
    }

    public AuthenticatedStudent parse(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return new AuthenticatedStudent(Long.valueOf(claims.getSubject()), claims.get("email", String.class));
    }
}
