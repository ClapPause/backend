package com.clap.pause.service.auth;

import com.clap.pause.config.properties.JwtProperties;
import com.clap.pause.dto.auth.AuthResponse;
import com.clap.pause.model.Member;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtProperties jwtProperties;

    private AuthResponse createAuthResponseWithMember(Member member) {
        var token = Jwts.builder()
                .subject(member.getId().toString())
                .claim("role", member.getMemberRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expiredTime()))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .compact();
        return AuthResponse.of(token);
    }
}
