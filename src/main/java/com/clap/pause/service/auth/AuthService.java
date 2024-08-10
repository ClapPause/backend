package com.clap.pause.service.auth;

import com.clap.pause.config.properties.JwtProperties;
import com.clap.pause.dto.auth.AuthResponse;
import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.DuplicatedEmailException;
import com.clap.pause.exception.InvalidLoginInfoException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Member;
import com.clap.pause.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final JwtProperties jwtProperties;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest registerRequest) {
        var member = saveMemberWithMemberRequest(registerRequest);
        return createAuthResponseWithMember(member);
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        var member = memberRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new NotFoundElementException(loginRequest.email() + "를 가진 이용자가 존재하지 않습니다."));

        var isMatched = passwordEncoder.matches(loginRequest.password(), member.getPassword());
        if (!isMatched) {
            throw new InvalidLoginInfoException("로그인 정보가 유효하지 않습니다.");
        }
        return createAuthResponseWithMember(member);
    }

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

    private void emailValidation(String email) {
        if (memberRepository.existsByEmail(email)) {
            throw new DuplicatedEmailException("이미 존재하는 이메일입니다.");
        }
    }

    private Member saveMemberWithMemberRequest(RegisterRequest registerRequest) {
        emailValidation(registerRequest.email());
        var encodedPassword = passwordEncoder.encode(registerRequest.password());
        var member = new Member(registerRequest.name(), registerRequest.email(), encodedPassword, registerRequest.profileImage(), registerRequest.birth(), registerRequest.gender(), registerRequest.job());
        return memberRepository.save(member);
    }
}
