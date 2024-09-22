package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.model.Comment;
import com.clap.pause.model.DepartmentGroup;
import com.clap.pause.model.Member;
import com.clap.pause.model.MemberUniversityDepartment;
import com.clap.pause.model.Post;
import com.clap.pause.model.UniversityDepartment;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private MemberUniversityDepartmentService memberUniversityDepartmentService;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("안내에 맞게 댓글을 요청하면 성공한다.")
    void saveComment_success() {
        //given
        var commentRequest = getCommentRequest();
        var member = mock(Member.class);
        var post = mock(Post.class);
        var departmentGroup = mock(DepartmentGroup.class);
        var universityDepartment = mock(UniversityDepartment.class);
        var memberUniversityDepartment = mock(MemberUniversityDepartment.class);
        var savedComment = new Comment(member, post, "댓글");

        when(member.getName()).thenReturn("Test");
        when(post.getDepartmentGroup()).thenReturn(departmentGroup);
        when(departmentGroup.getId()).thenReturn(1L);
        when(universityDepartment.getUniversity()).thenReturn("대학교");
        when(universityDepartment.getDepartment()).thenReturn("학과");
        when(memberUniversityDepartment.getId()).thenReturn(1L);
        when(memberUniversityDepartment.getMember()).thenReturn(member);
        when(memberUniversityDepartment.getUniversityDepartment()).thenReturn(universityDepartment);

        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(member));
        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(savedComment);
        when(memberUniversityDepartmentService.findProperMemberUniversityDepartment(any(Long.class), any(Long.class)))
                .thenReturn(memberUniversityDepartment);
        when(commentRepository.findAllByParentCommentId(any()))
                .thenReturn(new ArrayList<>());
        //when
        var comment = commentService.saveComment(1L, 1L, commentRequest);
        // then
        Assertions.assertThat(comment.contents())
                .isEqualTo("댓글");
    }

    private CommentRequest getCommentRequest() {
        return new CommentRequest("댓글");
    }
}
