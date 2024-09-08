package com.clap.pause.service;

import com.clap.pause.dto.departmentGroup.DepartmentGroupResponse;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityDepartmentResponse;
import com.clap.pause.dto.post.request.PostRequest;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.exception.PostAccessException;
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
import com.clap.pause.service.image.ImageService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @Mock
    private MemberUniversityDepartmentService memberUniversityDepartmentService;
    @Mock
    private DepartmentGroup departmentGroup;
    @Mock
    private List<MultipartFile> imageFiles;
    @Mock
    List<String> imageUrl;

    @Mock
    private ImageService imageService;


    @Test
    @DisplayName("글을 저장한다")
    void savePost_success() throws Exception {
        //given
        var memberId = 1L;
        var departmentGroupId = 1L;
        var departmentGroup = new DepartmentGroup("전자공학과");
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(imageService.saveImages(imageFiles)).thenReturn(imageUrl);
        when(memberRepository.findById(any())).thenReturn(Optional.of(getMember()));
        when(departmentGroupRepository.findById(any())).thenReturn(Optional.of(getDepartmentGroup()));
        when(postRepository.save(any(Post.class))).thenReturn(getPost(departmentGroup));
        //when
        var response = postService.saveDefaultPost(memberId, postRequest, departmentGroupId, imageFiles);
        //then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("제목");
        assertThat(response.postType()).isEqualTo(PostType.DEFAULT);
    }

    @Test
    @DisplayName("멤버가 존재하지 않으면 글 생성에 실패한다")
    void savePost_fail_MemberNotFound() {
        //given
        var memberId = 1L;
        var departmentGroupId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(memberRepository.findById(any())).thenThrow(new NotFoundElementException("존재하지 않는 이용자입니다."));
        //when, then
        assertThatThrownBy(() -> postService.saveDefaultPost(memberId, postRequest, departmentGroupId, imageFiles))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("학과그룹이 존재하지 않으면 글 생성에 실패한다")
    void savePost_fail_departmentGroupIsNotFound() throws Exception {
        //given
        var memberId = 1L;
        var departmentGroupId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(memberRepository.findById(any())).thenReturn(Optional.of(getMember()));
        when(departmentGroupRepository.findById(any())).thenThrow(new NotFoundElementException("존재하지 않는 학과그룹입니다."));
        //when, then
        assertThatThrownBy(() -> postService.saveDefaultPost(memberId, postRequest, departmentGroupId, imageFiles))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("학과그룹의 모든 게시글을 조회한다")
    void getAllPosts_success() throws Exception {
        //given
        var departmentGroupId = 2L;
        when(departmentGroupRepository.findById(any())).thenReturn(Optional.of(departmentGroup));
        when(postRepository.findByDepartmentGroupOrderByCreatedAtDesc(any())).thenReturn(getPostList(departmentGroup));
        when(memberUniversityDepartmentService.getMemberUniversityDepartments(any())).thenReturn(
                getMemberUniversityDepartmentResponse());
        when(departmentGroup.getId()).thenReturn(2L);
        //when
        var postListResponses = postService.getAllPosts(departmentGroupId);
        //then
        assertThat(postListResponses).hasSize(3);
        assertThat(postListResponses.get(0).departmentGroupId()).isEqualTo(2L);
        assertThat(postListResponses.get(1).memberName()).isEqualTo("가회원");
    }

    @Test
    @DisplayName("멤버가 해당 학과그룹에 권한이 없으면 모든 게시글을 조회에 실패한다")
    void getAllPosts_fail_postNotAccess() throws Exception {
        //given
        var departmentGroupId = 2L;
        when(departmentGroupRepository.findById(any())).thenReturn(Optional.of(departmentGroup));
        when(postRepository.findByDepartmentGroupOrderByCreatedAtDesc(any())).thenReturn(getPostList(departmentGroup));
        when(memberUniversityDepartmentService.getMemberUniversityDepartments(any())).thenReturn(
                getMemberUniversityDepartmentResponse());
        when(departmentGroup.getId()).thenReturn(3L);
        //when & then
        assertThatThrownBy(() -> postService.getAllPosts(departmentGroupId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("학과그룹이 없으면 모든 게시글을 조회에 실패한다")
    void getAllPosts_fail_universityGroupIsNotFound() throws Exception {
        //given
        var departmentGroupId = 2L;
        when(departmentGroupRepository.findById(any())).thenThrow(new NotFoundElementException("학과 그룹이 존재하지 않습니다."));
        //when
        //then
        assertThatThrownBy(() -> postService.getAllPosts(departmentGroupId))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("게시글을 조회한다")
    void getPostResponse_success() throws Exception {
        //given
        var postId = 1L;
        when(postRepository.findById(any())).thenReturn(Optional.of(getPost(departmentGroup)));
        when(memberUniversityDepartmentService.getMemberUniversityDepartments(any())).thenReturn(
                getMemberUniversityDepartmentResponse());
        when(departmentGroup.getId()).thenReturn(2L);
        //when
        var postResponse = postService.getPostResponse(postId);
        //then
        assertThat(postResponse.title()).isEqualTo("제목");
        assertThat(postResponse.memberName()).isEqualTo("가회원");
        assertThat(postResponse.departmentGroupId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("멤버가 해당 학과그룹에 권한이 없으면 게시글 조회에 실패한다")
    void getPostResponse_fail_postNotAccess() throws Exception {
        //given
        var postId = 1L;
        when(postRepository.findById(any())).thenReturn(Optional.of(getPost(departmentGroup)));
        when(memberUniversityDepartmentService.getMemberUniversityDepartments(any())).thenReturn(
                getMemberUniversityDepartmentResponse());
        when(departmentGroup.getId()).thenReturn(3L);
        //when & then
        assertThatThrownBy(() -> postService.getPostResponse(postId))
                .isInstanceOf(PostAccessException.class);
    }

    @Test
    @DisplayName("해당 post가 없다면 게시글 조회에 실패한다")
    void getPostResponse_fail_postIsNotFound() throws Exception {
        //given
        var postId = 1L;
        when(postRepository.findById(any())).thenThrow(new NotFoundElementException("글이 존재하지 않습니다."));
        //when & then
        assertThatThrownBy(() -> postService.getPostResponse(postId))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("게시글을 수정한다")
    void updatePost_success() throws Exception {
        //given
        var postId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(postRepository.findById(any())).thenReturn(Optional.of(getPost(departmentGroup)));
        //when
        postService.updatePost(postId, postRequest);
        //then
        verify(postRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("post가 없으면 게시글을 수정에 실패한다")
    void updatePost_fail_postIsNotFound() throws Exception {
        //given
        var postId = 1L;
        var postRequest = new PostRequest("제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
        when(postRepository.findById(any())).thenThrow(new NotFoundElementException("글이 존재하지 않습니다."));
        //when & then
        assertThatThrownBy(() -> postService.updatePost(postId, postRequest))
                .isInstanceOf(NotFoundElementException.class);
    }

    @Test
    @DisplayName("게시글을 삭제한다")
    void deletePost_success() throws Exception {
        //given
        var postId = 1L;
        when(postRepository.findById(any())).thenReturn(Optional.of(getPost(departmentGroup)));
        //when
        postService.deletePost(postId);
        //then
        verify(postRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("게시글을 삭제한다")
    void deletePost_fail_postIsNotFound() throws Exception {
        //given
        var postId = 1L;
        when(postRepository.findById(any())).thenThrow(new NotFoundElementException("글이 존재하지 않습니다."));
        //when & then
        assertThatThrownBy(() -> postService.deletePost(postId))
                .isInstanceOf(NotFoundElementException.class);
    }

    private List<Post> getPostList(DepartmentGroup departmentGroup) {
        var posts = new ArrayList<Post>();
        for (int i = 0; i < 3; i++) {
            posts.add(new Post(getMember(), departmentGroup, "제목" + i, "내용" + i, PostCategory.CONCERN, PostType.DEFAULT));
        }
        return posts;
    }

    private Post getPost(DepartmentGroup departmentGroup) {
        return new Post(getMember(), departmentGroup, "제목", "내용", PostCategory.CONCERN, PostType.DEFAULT);
    }

    private Member getMember() {
        return new Member("가회원", "test@naver.com", "password1234", LocalDate.now(), Gender.valueOf("FEMALE"), "student", "010-1234-1234");
    }

    private DepartmentGroup getDepartmentGroup() {
        return new DepartmentGroup("전자공학과");
    }

    private List<MemberUniversityDepartmentResponse> getMemberUniversityDepartmentResponse() {
        var responses = new ArrayList<MemberUniversityDepartmentResponse>();
        responses.add(new MemberUniversityDepartmentResponse(1L, new DepartmentGroupResponse(1L, "IT"), "충남대학교", "컴퓨터공학과", DepartmentType.MAJOR));
        responses.add(new MemberUniversityDepartmentResponse(2L, new DepartmentGroupResponse(2L, "전자"), "충남대학교", "전자공학과", DepartmentType.MINOR));
        return responses;
    }
}
