package com.clap.pause.service.auth;

import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.DuplicatedEmailException;
import com.clap.pause.model.Gender;
import com.clap.pause.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@SpringBootTest
@Transactional
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("회원가입 시도하기")
    void register_success() {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업");
        var auth = authService.register(registerRequest);
        //when, then
        Assertions.assertThatThrownBy(() -> authService.register(registerRequest)).isInstanceOf(DuplicatedEmailException.class);

        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        memberService.deleteMember(memberId);
    }
}
