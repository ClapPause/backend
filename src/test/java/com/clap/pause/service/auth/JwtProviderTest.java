package com.clap.pause.service.auth;

import com.clap.pause.config.properties.JwtProperties;
import com.clap.pause.model.Member;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtProviderTest {

    @Mock
    private JwtProperties jwtProperties;
    @Mock
    private Member member;
    @InjectMocks
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("Member 객체를 넘겨주면 JWT 토큰이 정상적으로 생성된다.")
    void generateToken_success() {
        //given
        when(member.getId())
                .thenReturn(1L);
        when(jwtProperties.secretKey())
                .thenReturn("CLAP-PAUSE-SERVER-MEMBER-SECRET-KEY");
        when(jwtProperties.expiredTime())
                .thenReturn(8640000L);
        //when
        var token = jwtProvider.generateToken(member);
        //then
        Assertions.assertThat(token)
                .isNotNull();
    }

    @Test
    @DisplayName("자체 시크릿키에 의해 암호화된 JWT 를 넘겨주면 정상적으로 Member Id 를 반환한다.")
    void getMemberIdWithToken_success() {
        // given
        when(jwtProperties.secretKey())
                .thenReturn("CLAP-PAUSE-SERVER-MEMBER-SECRET-KEY");
        when(jwtProperties.expiredTime())
                .thenReturn(8640000L);

        var token = Jwts.builder()
                .subject("1")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.expiredTime()))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .compact();
        // when
        var memberId = jwtProvider.getMemberIdWithToken(token);
        // then
        Assertions.assertThat(memberId)
                .isEqualTo(1L);
    }

    @Test
    @DisplayName("유효기간이 지난 JWT 를 넘겨주면 실패한다.")
    void getMemberIdWithToken_fail_expiredToken() {
        //given
        when(jwtProperties.secretKey())
                .thenReturn("CLAP-PAUSE-SERVER-MEMBER-SECRET-KEY");

        var token = Jwts.builder()
                .subject("1")
                .expiration(new Date(System.currentTimeMillis() - 10000))
                .signWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .compact();
        //when, then
        Assertions.assertThatThrownBy(() -> jwtProvider.getMemberIdWithToken(token))
                .isInstanceOf(ExpiredJwtException.class);
    }
}
