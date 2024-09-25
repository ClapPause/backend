package com.clap.pause.controller;


import com.clap.pause.config.security.JwtAuthFilter;
import com.clap.pause.config.security.MockMember;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.response.PostIdResponse;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import com.clap.pause.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PostController.class,
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class)
        })
@MockMember
@MockBean(JpaMetamodelMappingContext.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private PostService postService;

    @Test
    @DisplayName("게시글 생성")
    void savePost_success() throws Exception {
        //given
        var departmentGroupId = 1;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT, getImages());
        var response = new PostIdResponse(1L);
        when(postService.saveDefaultPost(any(), any(), any())).thenReturn(response);
        //when
        var perform = mockMvc.perform(post("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("글타입이 없으면 게시글 생성에 실패한다")
    void savePost_fail_postTypeIsNull() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, null, getImages());
        //when
        var perform = mockMvc.perform(post("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("글 내용이 없으면 게시글 생성에 실패한다")
    void savePost_fail_createdAtIsNull() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postRequest = new PostRequest("제목", "   ", PostCategory.CONCERN, PostType.DEFAULT, getImages());
        //when
        var perform = mockMvc.perform(post("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 생성 - 제목이 공백일 경우 실패")
    void savePost_fail_titleIsBlank() throws Exception {
        //given
        var postRequest = new PostRequest("", "내용", PostCategory.CONCERN, PostType.DEFAULT, getImages());
        //when
        var departmentGroupId = 1;
        var perform = mockMvc.perform(post("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("게시글 생성 - 글 타입이 공백일 경우 실패")
    void savePost_fail_postTypeIsBlank() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, null, getImages());
        //when
        var perform = mockMvc.perform(post("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("모든 게시글 불러오기에 성공한다")
    void getAllPosts_success() throws Exception {
        //given
        var departmentGroupId = 1L;
        when(postService.getAllPosts(any())).thenReturn(getPostListResponses());
        //when
        var perform = mockMvc.perform(get("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));
        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("제목1"))
                .andExpect(jsonPath("$[0].postType").value("DEFAULT"))
                .andExpect(jsonPath("$[1].contents").value("내용2"))
                .andExpect(jsonPath("$[1].memberName").value("가회원"))
                .andExpect(jsonPath("$[1].department").value("정보통신학과"))
                .andExpect(jsonPath("$[2].postCategory").value("CONCERN"))
                .andExpect(jsonPath("$[2].createdAt").value("2000-01-01T00:00:00"))
                .andExpect(jsonPath("$[2].university").value("충남대"));
    }

    private List<PostListResponse> getPostListResponses() {
        var responses = new ArrayList<PostListResponse>();
        responses.add(new PostListResponse(1L, 1L, "제목1", "내용1", PostCategory.CONCERN, PostType.DEFAULT, LocalDateTime.of(2000, 1, 12, 2, 24), "나회원", "고려대학교", "정보통신학과", getImages(), null, null));
        responses.add(new PostListResponse(2L, 1L, "제목2", "내용2", PostCategory.CONCERN, PostType.DEFAULT, LocalDateTime.of(2000, 1, 3, 2, 24), "가회원", "충남대", "정보통신학과", getImages(), null, null));
        responses.add(new PostListResponse(3L, 1L, "제목3", "내용3", PostCategory.CONCERN, PostType.DEFAULT, LocalDateTime.of(2000, 1, 1, 0, 0, 0), "다회원", "충남대", "정보통신학과", getImages(), null, null));
        return responses;
    }

    @Test
    @DisplayName("학교그룹이 존재하지 않으면 게시글 조회가 실패한다")
    void getAllPosts_fail_notExistDepartmentId() throws Exception {
        //given
        var departmentGroupId = 1L;
        when(postService.getAllPosts(any())).thenThrow(new NotFoundElementException("학과 그룹이 존재하지 않습니다."));
        //when
        var perform = mockMvc.perform(get("/api/department-groups/" + departmentGroupId + "/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));
        //then
        perform.andExpect(status().isNotFound())
                .andExpect(
                        result -> assertThat(getApiResultExceptionClass(result)).isEqualTo(
                                NotFoundElementException.class));
    }

    private Class<? extends Exception> getApiResultExceptionClass(MvcResult result) {
        return Objects.requireNonNull(result.getResolvedException()).getClass();
    }

    @Test
    @DisplayName("글 id로 글을 조회한다")
    void getPost_success() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postId = 1L;
        var response = new PostListResponse(1L, 1L, "제목1", "내용1", PostCategory.CONCERN, PostType.DEFAULT, LocalDateTime.of(2000, 1, 12, 2, 24), "나회원", "고려대학교", "정보통신학과", null, null, null);
        when(postService.getPost(any())).thenReturn(response);
        //when
        ResultActions perform =
                mockMvc.perform(get("/api/department-groups/" + departmentGroupId + "/posts/" + postId)
                        .with(csrf()));
        //then
        perform.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("제목1"))
                .andExpect(jsonPath("$.memberName").value("나회원"))
                .andExpect(jsonPath("$.university").value("고려대학교"));
    }

    @Test
    @DisplayName("글을 수정한다")
    void updatePost_success() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postId = 1L;
        var postRequest = new PostRequest("수정된 제목", "수정된 내용", PostCategory.CONCERN, PostType.DEFAULT, getImages());
        //when
        var perform = mockMvc.perform(put("/api/department-groups/" + departmentGroupId + "/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));

        //then
        perform.andExpect(status().isNoContent());
        verify(postService, times(1)).updatePost(any(), any());
    }

    @Test
    @DisplayName("제목이 공백이면 글 수정을 실패한다")
    void updatePost_fail_titleIsBlank() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postId = 1L;
        var postRequest = new PostRequest("", "수정된 내용", PostCategory.CONCERN, PostType.DEFAULT, getImages());
        //when
        var perform = mockMvc.perform(put("/api/department-groups/" + departmentGroupId + "/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("글 카테고리를 선택하지 않으면 글 수정을 실패한다")
    void updatePost_fail_CategoryIsNull() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postId = 1L;
        var postRequest = new PostRequest("", "수정된 내용", null, PostType.DEFAULT, getImages());
        //when
        var perform = mockMvc.perform(put("/api/department-groups/" + departmentGroupId + "/posts/" + postId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postRequest))
                .with(csrf()));
        //then
        perform.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("글을 삭제한다")
    void deletePost_success() throws Exception {
        //given
        var departmentGroupId = 1L;
        var postId = 1L;
        //when
        var perform =
                mockMvc.perform(delete("/api/department-groups/" + departmentGroupId + "/posts/" + postId)
                        .with(csrf()));
        //then
        perform.andExpect(status().isNoContent());
        verify(postService, times(1)).deletePost(1L);
    }

    private List<String> getImages() {
        var images = new ArrayList<String>();
        String image1 = "image1.jpg";
        String image2 = "image2.jpg";
        String image3 = "image3.jpg";
        images.add(image1);
        images.add(image2);
        images.add(image3);
        return images;
    }
}
