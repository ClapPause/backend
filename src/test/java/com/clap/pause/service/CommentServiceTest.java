package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.exception.InvalidRequestException;
import com.clap.pause.model.Comment;
import com.clap.pause.provider.EntityProvider;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
    @Mock
    private CommentLikeService commentLikeService;
    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("안내에 맞게 댓글 생성을 요청하면 성공한다.")
    void saveComment_success() {
        //given
        var commentRequest = getSaveCommentRequest();
        var member = EntityProvider.getMember(1L);
        var post = EntityProvider.getPost(1L);
        var comment = EntityProvider.getComment(1L);

        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(member));
        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        //when
        commentService.saveComment(1L, 1L, commentRequest);
        //then
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("안내에 맞게 대댓글 생성을 요청하면 성공한다.")
    void saveReply_success() {
        //given
        var commentRequest = getSaveCommentRequest();
        var member = EntityProvider.getMember(1L);
        var post = EntityProvider.getPost(1L);
        var parentComment = EntityProvider.getComment(1L);
        var comment = EntityProvider.getComment(2L);

        when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(member));
        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(post));
        when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(parentComment));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        //when
        commentService.saveReply(1L, 1L, 1L, commentRequest);
        //then
        verify(commentRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("안내에 맞게 댓글 수정을 요청하면 성공한다.")
    void updateComment_success() {
        //given
        var commentRequest = getUpdateCommentRequest();
        var comment = EntityProvider.getComment(1L);

        when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(comment));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        //when
        commentService.updateComment(1L, 1000L, commentRequest);
        //then
        Assertions.assertThat(comment.getContents())
                .isEqualTo("댓글 수정");
    }

    @Test
    @DisplayName("잘못된 게시글 ID를 통해 댓글 수정을 요청하면 실패한다.")
    void updateComment_fail_invalidPostId() {
        //given
        var commentRequest = getUpdateCommentRequest();
        var comment = EntityProvider.getComment(1L);

        when(commentRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(comment));
        //when, then
        Assertions.assertThatThrownBy(() -> commentService.updateComment(1L, 1L, commentRequest))
                .isInstanceOf(InvalidRequestException.class);
    }

    @Test
    @DisplayName("댓글과 대댓글에 대한 조회를 요청하면 계층형 구조로 결과를 반환한다.")
    void getComments_success() {
        //given
        var map = new HashMap<Long, Integer>();
        map.put(1L, 3);
        when(commentLikeService.getCommentLikeCount(any(Long.class)))
                .thenReturn(map);
        var comment1 = EntityProvider.getComment(1L);
        var comment2 = EntityProvider.getComment(2L, 1L);
        var comment3 = EntityProvider.getComment(3L, 1L);
        var comment4 = EntityProvider.getComment(4L);
        var comments = List.of(comment1, comment2, comment3, comment4);
        var memberUniversityDepartment = EntityProvider.getMemberUniversityDepartment(1L);

        when(commentRepository.findAllByPostIdOrderByCreatedAt(any(Long.class))).thenReturn(comments);
        when(memberUniversityDepartmentService.findProperMemberUniversityDepartment(any(Long.class), any(Long.class))).thenReturn(memberUniversityDepartment);
        when(postRepository.existsById(any(Long.class))).thenReturn(true);
        //when
        var result = commentService.getComments(1L);
        //then
        Assertions.assertThat(result.size())
                .isEqualTo(2);
        Assertions.assertThat(result.get(0).likeCount())
                .isEqualTo(3);
        Assertions.assertThat(result.get(0).replies().size())
                .isEqualTo(2);
    }

    @Test
    @DisplayName("댓글을 조회하면 댓글의 좋아요 수도 확인할 수 있다.")
    void getComments_success_likeCount() {
        //given
        var comment = EntityProvider.getComment(1L);
        var comments = List.of(comment);
        var memberUniversityDepartment = EntityProvider.getMemberUniversityDepartment(1L);
        var map = new HashMap<Long, Integer>();
        map.put(1L, 3);

        when(commentLikeService.getCommentLikeCount(any(Long.class))).thenReturn(map);
        when(commentRepository.findAllByPostIdOrderByCreatedAt(any(Long.class))).thenReturn(comments);
        when(memberUniversityDepartmentService.findProperMemberUniversityDepartment(any(Long.class), any(Long.class))).thenReturn(memberUniversityDepartment);
        when(postRepository.existsById(any(Long.class))).thenReturn(true);
        //when
        var result = commentService.getComments(1L);
        //then
        Assertions.assertThat(result.size())
                .isEqualTo(1);
        Assertions.assertThat(result.get(0).likeCount())
                .isEqualTo(3);
    }

    @Test
    @DisplayName("댓글에 대한 삭제를 요청하면 성공한다.")
    void deleteComment_success() {
        //given
        var comment = EntityProvider.getComment(1L);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(any(Comment.class));
        //when
        commentService.deleteComment(1000L, 1L);
        //then
        verify(commentRepository, times(1)).delete(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 Post ID 로 삭제를 요청하면 실패한다.")
    void deleteComment_fail_invalidPostId() {
        //given
        var comment = EntityProvider.getComment(1L);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        //when, then
        Assertions.assertThatThrownBy(() -> commentService.deleteComment(1001L, 1L))
                .isInstanceOf(InvalidRequestException.class);
    }

    private CommentRequest getSaveCommentRequest() {
        return new CommentRequest("댓글");
    }

    private CommentRequest getUpdateCommentRequest() {
        return new CommentRequest("댓글 수정");
    }
}
