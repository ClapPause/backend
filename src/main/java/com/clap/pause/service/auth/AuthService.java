package com.clap.pause.service.auth;

import com.clap.pause.dto.auth.AuthResponse;
import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.DuplicatedException;
import com.clap.pause.exception.InvalidLoginInfoException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Member;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;

    /**
     * 회원가입을 처리하는 메서드
     *
     * @param registerRequest
     * @return authResponse
     */
    public AuthResponse register(RegisterRequest registerRequest) {
        validate(registerRequest);
        var member = saveMemberWithMemberRequest(registerRequest);
        notificationService.saveNotification(member);
        return createAuthResponseWithMember(member);
    }

    /**
     * 로그인을 처리하는 메서드
     *
     * @param loginRequest
     * @return authResponse
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest loginRequest) {
        var member = memberRepository.findByPhoneNumber(loginRequest.phoneNumber())
                .orElseThrow(() -> new NotFoundElementException(loginRequest.phoneNumber() + "를 가진 이용자가 존재하지 않습니다."));
        if (!passwordEncoder.matches(loginRequest.password(), member.getPassword())) {
            throw new InvalidLoginInfoException("로그인 정보가 유효하지 않습니다.");
        }
        return createAuthResponseWithMember(member);
    }

    /**
     * JWT 를 생성하는 메서드
     *
     * @param member
     * @return authResponse
     */
    private AuthResponse createAuthResponseWithMember(Member member) {
        return AuthResponse.of(jwtProvider.generateToken(member));
    }

    /**
     * 입력된 값에 대한 비즈니스 로직 검증 수행하는 메서드
     *
     * @param registerRequest
     */
    private void validate(RegisterRequest registerRequest) {
        phoneNumberValidation(registerRequest.phoneNumber());
        nameValidation(registerRequest.name());
    }

    /**
     * 중복된 이메일을 검증하는 메서드
     *
     * @param phoneNumber
     */
    private void phoneNumberValidation(String phoneNumber) {
        if (memberRepository.existsByPhoneNumber(phoneNumber)) {
            throw new DuplicatedException("이미 존재하는 연락처입니다.");
        }
    }

    /**
     * 중복된 닉네임을 검증하는 메서드
     *
     * @param name
     */
    private void nameValidation(String name) {
        if (memberRepository.existsByName(name)) {
            throw new DuplicatedException("이미 존재하는 닉네임입니다.");
        }
    }

    /**
     * Member 객체를 생성하는 메서드
     *
     * @param registerRequest
     * @return member
     */
    private Member saveMemberWithMemberRequest(RegisterRequest registerRequest) {
        var encodedPassword = passwordEncoder.encode(registerRequest.password());
        var member = new Member(registerRequest.name(), registerRequest.phoneNumber(), encodedPassword, registerRequest.birth(), registerRequest.gender(), registerRequest.job());
        return memberRepository.save(member);
    }
}
