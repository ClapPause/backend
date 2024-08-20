package com.clap.pause.controller.auth;

import com.clap.pause.dto.auth.AuthResponse;
import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.exception.ExceptionResponse;
import com.clap.pause.model.Gender;
import com.clap.pause.service.MemberService;
import com.clap.pause.service.auth.AuthService;
import com.clap.pause.service.auth.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;

    @Test
    @DisplayName("안내에 맞게 회원가입을 요청하면 성공한다")
    void register_success() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isOk()).andReturn();
        var response = getResponse(postResult);

        var memberId = jwtProvider.getMemberIdWithToken(response.token());
        memberService.deleteMember(memberId);
    }

    @Test
    @DisplayName("이름의 길이가 8글자를 넘어가면 회원가입을 실패한다.")
    void register_fail_nameLengthOver() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트테스트테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("이름은 최소 1글자, 최대 8글자까지 가능합니다.");
    }

    @Test
    @DisplayName("허용되지 않는 형식의 이메일로 회원가입을 요청하면 회원가입을 실패한다.")
    void register_fail_invalidEmail() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@test", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("허용되지 않은 형식의 이메일입니다.");
    }

    @Test
    @DisplayName("허용되지 않는 형식의 패스워드로 회원가입을 요청하면 회원가입을 실패한다.")
    void register_fail_invalidPassword() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "wrongpw", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("허용되지 않은 형식의 패스워드입니다.");
    }

    @Test
    @DisplayName("유효하지 않은 생일로 회원가입을 요청하면 회원가입을 실패한다.")
    void register_fail_invalidBirth() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(2099, 12, 25), Gender.MALE, "직업", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("유효하지 않은 생일입니다.");
    }

    @Test
    @DisplayName("직업이 비어있는 상태로 회원가입을 요청하면 회원가입을 실패한다.")
    void register_fail_invalidGender() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "", "010-1234-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("직업은 반드시 입력되어야 합니다.");
    }

    @Test
    @DisplayName("연락처가 잘못된 형식으로 회원가입을 요청하면 회원가입을 실패한다.")
    void register_fail_invalidPhone() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234");
        var postRequest = post("/api/members/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("허용되지 않은 형식의 연락처입니다.");
    }

    @Test
    @DisplayName("안내에 맞게 회원가입을 요청하면 성공한다")
    void login_success() throws Exception {
        //given
        var registerRequest = new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE, "직업", "010-1234-1234");
        authService.register(registerRequest);

        var loginRequest = new LoginRequest("test@naver.com", "testPassword");
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        result.andExpect(status().isOk());
    }

    @Test
    @DisplayName("허용되지 않는 형식의 이메일로 로그인을 요청하면 실패한다.")
    void login_fail_invalidEmail() throws Exception {
        //given
        var loginRequest = new LoginRequest("test@test", "testPassword");
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("허용되지 않은 형식의 이메일입니다.");
    }

    @Test
    @DisplayName("허용되지 않는 형식의 패스워드로 로그인을 요청하면 실패한다.")
    void login_fail_invalidPassword() throws Exception {
        //given
        var loginRequest = new LoginRequest("test@naver.com", "wrongpw");
        var postRequest = post("/api/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest));
        //when
        var result = mockMvc.perform(postRequest);
        //then
        var postResult = result.andExpect(status().isBadRequest()).andReturn();
        var response = getExceptionResponseMessage(postResult);

        Assertions.assertThat(response.status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        Assertions.assertThat(response.message()).isEqualTo("허용되지 않은 형식의 패스워드입니다.");
    }

    private AuthResponse getResponse(MvcResult mvcResult) throws Exception {
        var response = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(response, AuthResponse.class);
    }

    private ExceptionResponse getExceptionResponseMessage(MvcResult result) throws Exception {
        var resultString = result.getResponse().getContentAsString();
        return objectMapper.readValue(resultString, ExceptionResponse.class);
    }
}
