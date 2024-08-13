package com.clap.pause.service.auth;

import com.clap.pause.config.properties.JwtProperties;
import com.clap.pause.model.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final JwtProperties jwtProperties;

    /**
     * JWT 를 생성하는 메서드
     *
     * @param member
     * @return authResponse
     */
    public String generateToken(Member member) {
        return Jwts.builder()
                .subject(member.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expiredTime()))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .compact();
    }

    /**
     * Member Id 를 반환하는 메서드
     *
     * @param jwt
     * @return memberId
     */
    public Long getMemberIdWithToken(String jwt) {
        var claims = decryptToken(jwt);
        return Long.parseLong(claims.getSubject());
    }

    /**
     * JWT 를 복호화하는 메서드
     *
     * @param jwt
     * @return claims
     */
    private Claims decryptToken(String jwt) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}
