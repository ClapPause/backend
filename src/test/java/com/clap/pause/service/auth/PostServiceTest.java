package com.clap.pause.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.dto.post.response.PostListResponse;
import com.clap.pause.dto.post.response.PostResponse;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.model.DepartmentType;
import com.clap.pause.model.Gender;
import com.clap.pause.model.Member;
import com.clap.pause.model.Post;
import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import com.clap.pause.repository.DepartmentGroupRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import com.clap.pause.service.PostService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    @InjectMocks
    PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private DepartmentGroupRepository departmentGroupRepository;
    @Mock
    private MemberRepository memberRepository;


    @Test
    @DisplayName("글을 저장한다")
    void savePost_success() throws Exception {
        //given
        Long memberId = 1L;
        Long departmentGroupId = 1L;
        PostRequest postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
//        PostResponse response = new PostResponse(1L, "제목", "내용", PostCategory.CONCERN, PostType.DEFAULT,
//                LocalDateTime.of(2024, 8, 31, 2, 0));
        when(memberRepository.findById(any())).thenReturn(Optional.of(getMember()));
        when(departmentGroupRepository.findById(any())).thenReturn(Optional.of(getDepartmentGroup()));
        when(postRepository.save(any(Post.class))).thenReturn(getPost());
        //when
        PostResponse response = postService.saveDefaultPost(memberId, postRequest, departmentGroupId);
        //then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.postType()).isEqualTo(PostType.DEFAULT);
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 글 생성에 실패한다")
    void savePost_fail_MemberNotFound() throws Exception{
        //given
        Long memberId = 1L;
        Long departmentGroupId = 1L;
        PostRequest postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(memberRepository.findById(any())).thenThrow(new NotFoundElementException("멤버가 존재하지 않습니다"));
        //when
        PostResponse response = postService.saveDefaultPost(memberId, postRequest, departmentGroupId);
        //then
        assertThat()
    }


    private PostListResponse getPostListResponse() {
        return new PostListResponse(1L, 1L, "제목1", "내용1", PostCategory.CONCERN, PostType.DEFAULT, LocalDateTime.now(),
                "나회원", "고려대학교", "정보통신학과");
    }

    private List<Post> getPostList() {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            posts.add(new Post(getMember(), getDepartmentGroup(), "제목" + i, "내용" + i, PostCategory.CONCERN,
                    PostType.DEFAULT));
        }
        return posts;
    }

    private Post getPost() {
        return new Post(getMember(), getDepartmentGroup(), "제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
    }

    private Member getMember() {
        return new Member("가회원", "test@naver.com", "password1234", "img", LocalDate.now(), Gender.valueOf("FEMALE"),
                "student", "010-1234-1233");
    }

    private DepartmentGroup getDepartmentGroup() {
        return new DepartmentGroup("전자공학과");
    }

    private List<MemberUniversityDepartmentResponse> getMemberUniversityDepartmentResponse() {
        List<MemberUniversityDepartmentResponse> responses = new ArrayList<>();
        responses.add(
                new MemberUniversityDepartmentResponse(1L, new DepartmentGroupResponse(1L, "IT"), "충남대학교", "컴퓨터공학과",
                        DepartmentType.MAJOR));
        responses.add(
                new MemberUniversityDepartmentResponse(1L, new DepartmentGroupResponse(2L, "전자"), "충남대학교", "전자공학과",
                        DepartmentType.MINOR));
        return responses;
    }
}
