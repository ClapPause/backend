package com.clap.pause.controller.auth;

import com.clap.pause.dto.auth.LoginRequest;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentRequest;
import com.clap.pause.model.DepartmentType;
import com.clap.pause.service.MemberUniversityDepartmentService;
import com.clap.pause.service.PostService;
import com.clap.pause.service.auth.AuthService;
import com.clap.pause.service.auth.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private JwtProvider jwtProvider;
    @Autowired
    private MemberUniversityDepartmentService memberUniversityDepartmentService;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        var auth = authService.login(new LoginRequest("test@naver.com", "testPassword"));
        var memberId = jwtProvider.getMemberIdWithToken(auth.token());
        var memberUniversityDepartmentRequest = new MemberUniversityDepartmentRequest(1L, DepartmentType.MAJOR);
        memberUniversityDepartmentService.saveMemberUniversityDepartment(memberId, memberUniversityDepartmentRequest);
    }

    @Test
    @DisplayName("게시글 불러오기에 성공한다")
    void getPosts_success() throws Exception {
        //given

        //when
        //then


    }

}
