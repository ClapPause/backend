package com.clap.pause.controller.auth;

import com.clap.pause.dto.auth.RegisterRequest;
import com.clap.pause.model.Gender;
import com.clap.pause.service.PostService;
import com.clap.pause.service.auth.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostService postService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        var registerRequest =
                new RegisterRequest("테스트", "test@naver.com", "testPassword", LocalDate.of(1999, 1, 16), Gender.MALE,
                        "직업", "010-1234-1234");
        authService.register(registerRequest);
    }

    @Test
    @DisplayName("게시글 불러오기에 성공한다")
    void getPosts_success() throws Exception {
        //given

        //when
        //then


    }

}
