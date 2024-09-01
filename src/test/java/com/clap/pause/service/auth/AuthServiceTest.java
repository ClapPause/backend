package com.clap.pause.service.auth;

import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.DuplicatedEmailException;
import com.clap.pause.exception.InvalidLoginInfoException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Gender;
import com.clap.pause.model.Member;
import com.clap.pause.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    @DisplayName("안내에 맞게 회원가입을 요청하면 성공한다")
    void register_success() {
        //given
        var registerRequest = getRegisterRequest();
        var savedMember = getSavedMember();

        when(memberRepository.existsByEmail(any(String.class)))
                .thenReturn(false);
        when(memberRepository.save(any(Member.class)))
                .thenReturn(savedMember);
        when(jwtProvider.generateToken(any(Member.class)))
                .thenReturn("mockedJwtToken");
        //when
        var auth = authService.register(registerRequest);
        // then
        Assertions.assertThat(auth.token())
                .isEqualTo("mockedJwtToken");
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입을 요청하면 실패한다")
    void register_fail_duplicatedEmail() {
        //given
        var registerRequest = getRegisterRequest();

        when(memberRepository.existsByEmail(any(String.class)))
                .thenReturn(true);
        //when, then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(DuplicatedEmailException.class);
    }

    @Test
    @DisplayName("안내에 맞게 로그인을 요청하면 성공한다")
    void login_success() {
        //given
        var loginRequest = getLoginRequest();
        var member = getSavedMember();

        when(memberRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(member));
        when(jwtProvider.generateToken(any(Member.class)))
                .thenReturn("mockedJwtToken");
        //when
        var auth = authService.login(loginRequest);
        //then
        Assertions.assertThat(auth.token())
                .isEqualTo("mockedJwtToken");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 요청하면 실패한다")
    void login_fail_noExistEmail() {
        //given
        var loginRequest = getLoginRequest();

        when(memberRepository.findByEmail(any(String.class)))
                .thenThrow(new NotFoundElementException("존재하지 않는 이메일입니다."));
        //when, then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("잘못된 패스워드로 로그인을 요청하면 실패한다")
    void login_fail_invalidPassword() {
        //given
        var loginRequest = getErrorLoginRequest();
        var member = getSavedMember();

        when(memberRepository.findByEmail(any(String.class)))
                .thenReturn(Optional.of(member));
        //when, then
        Assertions.assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidLoginInfoException.class);
    }

    private RegisterRequest getRegisterRequest() {
        return new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
    }

    private LoginRequest getLoginRequest() {
        return new LoginRequest("test@naver.com", "testPassword");
    }

    private LoginRequest getErrorLoginRequest() {
        return new LoginRequest("test@naver.com", "errorPassword");
    }

    private Member getSavedMember() {
        var encryptedPassword = passwordEncoder.encode("testPassword");
        return new Member("테스트", "test@naver.com", encryptedPassword, LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
    }
}
