package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.CommentResponse;
import com.clap.pause.dto.comment.CommentSaveResponse;
import com.clap.pause.dto.comment.ReplyRequest;
import com.clap.pause.dto.comment.ReplyResponse;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;
import com.clap.pause.exception.InvalidRequestException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Comment;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final PostRepository postRepository;

    public CommentSaveResponse saveComment(Long memberId, Long postId, CommentRequest commentRequest) {
        var comment = saveCommentWithCommentRequest(memberId, postId, commentRequest);
        return getCommentSaveResponseWithComment(comment);
    }

    public CommentSaveResponse saveReply(Long memberId, Long postId, Long parentCommentId, ReplyRequest replyRequest) {
        var reply = saveReplyWithReplyRequest(memberId, postId, parentCommentId, replyRequest);
        return getCommentSaveResponseWithComment(reply);
    }

    // 조회 기능 구현
    public void updateComment(Long id, Long postId, CommentRequest commentRequest) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        if (!comment.getPost().getId().equals(postId)) {
            throw new InvalidRequestException(postId + "를 가진 게시글에 존재하지 않는 댓글입니다.");
        }
        updateCommentWithCommentUpdateRequest(comment, commentRequest);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        var comments = commentRepository.findAllByPostId(postId);
        return getCommentResponses(comments);
    }

    public void deleteComment(Long postId, Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        if (!comment.getPost().getId().equals(postId)) {
            throw new InvalidRequestException(postId + "를 가진 게시글에 존재하지 않는 댓글입니다.");
        }
        commentRepository.deleteAllByParentCommentId(id);
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

    private Comment saveReplyWithReplyRequest(Long memberId, Long postId, Long parentCommentId, ReplyRequest replyRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new NotFoundElementException(parentCommentId + "를 가진 댓글이 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, parentComment, replyRequest.contents());
        return commentRepository.save(comment);
    }

    private CommentSaveResponse getCommentSaveResponseWithComment(Comment comment) {
        return new CommentSaveResponse(comment.getId(), comment.getContents());
    }

    private List<CommentResponse> getCommentResponses(List<Comment> comments) {
        var queue = new LinkedList<>(comments);
        var commentResponseMap = new HashMap<Long, CommentResponse>();
        while (!queue.isEmpty()) {
            var comment = queue.poll();
            if (comment.getParentComment() == null) {
                var commentResponse = getCommentResponseWithComment(comment.getMember().getId(), comment);
                commentResponseMap.put(comment.getId(), commentResponse);
                continue;
            }
            if (commentResponseMap.containsKey(comment.getParentComment().getId())) {
                var response = commentResponseMap.get(comment.getParentComment().getId());
                response.replies().add(getReplyResponseWithComment(comment.getMember().getId(), comment));
                commentResponseMap.put(comment.getParentComment().getId(), response);
                continue;
            }
            queue.add(comment);
        }

        var result = new ArrayList<>(commentResponseMap.values());
        result.sort((a, b) -> b.createdAt().compareTo(a.createdAt()));
        return result;
    }

    private CommentResponse getCommentResponseWithComment(Long memberId, Comment comment) {
        var departmentGroupId = comment.getPost()
                .getDepartmentGroup()
                .getId();
        var memberUniversityDepartment = memberUniversityDepartmentService.findProperMemberUniversityDepartment(memberId, departmentGroupId);
        var universityDepartment = memberUniversityDepartment.getUniversityDepartment();
        var memberUniversityInfo = new MemberUniversityInfo(
                memberUniversityDepartment.getId(),
                memberUniversityDepartment.getMember().getName(),
                universityDepartment.getUniversity(),
                universityDepartment.getDepartment()
        );
        var replies = commentRepository.findAllByParentCommentId(comment.getId())
                .stream()
                .map(reply -> getReplyResponseWithComment(memberId, reply))
                .toList();
        return new CommentResponse(comment.getId(), memberUniversityInfo, comment.getContents(), comment.getCreatedAt(), replies);
    }

    private ReplyResponse getReplyResponseWithComment(Long memberId, Comment comment) {
        var departmentGroupId = comment.getPost()
                .getDepartmentGroup()
                .getId();
        var memberUniversityDepartment = memberUniversityDepartmentService.findProperMemberUniversityDepartment(memberId, departmentGroupId);
        var universityDepartment = memberUniversityDepartment.getUniversityDepartment();
        var memberUniversityInfo = new MemberUniversityInfo(memberUniversityDepartment.getId(), memberUniversityDepartment.getMember().getName(), universityDepartment.getUniversity(), universityDepartment.getDepartment());
        return new ReplyResponse(comment.getId(), memberUniversityInfo, comment.getContents(), comment.getCreatedAt());
    }

    private void updateCommentWithCommentUpdateRequest(Comment comment, CommentRequest commentRequest) {
        comment.updateContents(commentRequest.contents());
        commentRepository.save(comment);
    }
}
