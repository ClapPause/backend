package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.CommentResponse;
import com.clap.pause.dto.comment.CommentUpdateRequest;
import com.clap.pause.dto.comment.ReplyRequest;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Comment;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final PostRepository postRepository;

    public CommentResponse saveComment(Long memberId, Long postId, CommentRequest commentRequest) {
        var comment = saveCommentWithCommentRequest(memberId, postId, commentRequest);
        return getCommentResponse(memberId, comment);
    }

    public CommentResponse saveReply(Long memberId, Long postId, ReplyRequest replyRequest) {
        var comment = saveReplyWithReplyRequest(memberId, postId, replyRequest);
        return getCommentResponse(memberId, comment);
    }

    // 조회 기능 구현

    public void updateComment(Long id, Long postId, CommentUpdateRequest commentUpdateRequest) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        updateCommentWithCommentUpdateRequest(comment, commentUpdateRequest);
    }

    public void deleteComment(Long postId, Long id) {
        if (!commentRepository.existsById(id)) {
            throw new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다.");
        }
        commentRepository.deleteById(id);
    }

    private Comment saveCommentWithCommentRequest(Long memberId, Long postId, CommentRequest commentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, commentRequest.contents());
        return commentRepository.save(comment);
    }

    private Comment saveReplyWithReplyRequest(Long memberId, Long postId, ReplyRequest replyRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var parentComment = commentRepository.findById(replyRequest.parentCommentId())
                .orElseThrow(() -> new NotFoundElementException(replyRequest.parentCommentId() + "를 가진 댓글이 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, parentComment, replyRequest.contents());
        return commentRepository.save(comment);
    }

    private CommentResponse getCommentResponse(Long memberId, Comment comment) {
        var departmentGroupId = comment.getPost()
                .getDepartmentGroup()
                .getId();
        var memberUniversityDepartment = memberUniversityDepartmentService.findProperMemberUniversityDepartment(memberId, departmentGroupId);
        var universityDepartment = memberUniversityDepartment.getUniversityDepartment();
        var memberUniversityInfo = new MemberUniversityInfo(memberUniversityDepartment.getId(), memberUniversityDepartment.getMember().getName(), universityDepartment.getUniversity(), universityDepartment.getDepartment());
        return new CommentResponse(comment.getId(), memberUniversityInfo, comment.getContents(), comment.getCreatedAt());
    }

    private void updateCommentWithCommentUpdateRequest(Comment comment, CommentUpdateRequest commentUpdateRequest) {
        comment.updateContents(commentUpdateRequest.contents());
        commentRepository.save(comment);
    }
}
