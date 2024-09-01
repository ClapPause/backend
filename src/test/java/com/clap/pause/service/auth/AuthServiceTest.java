package com.clap.pause.service.auth;

import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.DuplicatedEmailException;
import com.clap.pause.exception.InvalidLoginInfoException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Gender;
import com.clap.pause.model.Member;
import com.clap.pause.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
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
        assertThat(auth).isNotNull();
        assertThat(auth.token()).isEqualTo("mockedJwtToken");
    }

    @Test
    @DisplayName("중복된 이메일로 회원가입을 요청하면 실패한다")
    void register_fail_duplicatedEmail() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var auth = authService.register(registerRequest);
        //when, then
        assertThatThrownBy(() -> authService.register(registerRequest)).isInstanceOf(DuplicatedEmailException.class);

        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        memberService.deleteMember(memberId);
    }

    @Test
    @DisplayName("안내에 맞게 로그인을 요청하면 성공한다")
    void login_success() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        authService.register(registerRequest);
        var loginRequest = new LoginRequest("test@naver.com", "testPassword");
        //when
        var auth = authService.login(loginRequest);
        //then
        assertThat(auth).isNotNull();

        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        memberService.deleteMember(memberId);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인을 요청하면 실패한다")
    void login_fail_noExistEmail() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var auth = authService.register(registerRequest);
        var loginRequest = new LoginRequest("test1@naver.com", "testPassword");
        //when, then
        assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(NotFoundElementException.class);

        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        memberService.deleteMember(memberId);
    }

    @Test
    @DisplayName("잘못된 패스워드로 로그인을 요청하면 실패한다")
    void login_fail_invalidPassword() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var auth = authService.register(registerRequest);
        var loginRequest = new LoginRequest("test@naver.com", "testPassword2");
        //when, then
        assertThatThrownBy(() -> authService.login(loginRequest)).isInstanceOf(InvalidLoginInfoException.class);

        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        memberService.deleteMember(memberId);
    }

    private RegisterRequest getRegisterRequest() {
        return new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
    }

    private Member getSavedMember() {
        var encryptedPassword = passwordEncoder.encode("testPassword");
        return new Member("테스트", "test@naver.com", encryptedPassword, LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
    }
}
