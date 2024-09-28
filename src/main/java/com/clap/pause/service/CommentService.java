package com.clap.pause.service;

import com.clap.pause.dto.comment.CommentRequest;
import com.clap.pause.dto.comment.CommentResponse;
import com.clap.pause.dto.comment.MemberCommentResponse;
import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.Comment;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import com.clap.pause.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final MemberUniversityDepartmentService memberUniversityDepartmentService;
    private final PostRepository postRepository;
    private final CommentLikeService commentLikeService;

    public void saveComment(Long memberId, Long postId, CommentRequest commentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, commentRequest.contents());
        commentRepository.save(comment);
    }

    public void saveReply(Long memberId, Long postId, Long parentCommentId, CommentRequest commentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new NotFoundElementException(parentCommentId + "를 가진 댓글이 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var reply = new Comment(member, post, parentComment, commentRequest.contents());
        commentRepository.save(reply);
    }

    public void updateComment(Long id, Long postId, CommentRequest commentRequest) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        comment.validatePost(postId);
        updateCommentWithCommentUpdateRequest(comment, commentRequest);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        var comments = commentRepository.findAllByPostIdOrderByCreatedAt(postId);
        var commentResponseMap = getCommentResponseMap(postId, comments);

        var result = new ArrayList<>(commentResponseMap.values());
        result.sort(Comparator.comparing(CommentResponse::createdAt));
        return result;
    }

    @Transactional(readOnly = true)
    public List<MemberCommentResponse> getCommentsWithMember(Long memberId) {
        var comments = commentRepository.findAllByMemberIdOrderByCreatedAt(memberId);

        return comments.stream()
                .map(this::getMemberCommentResponse)
                .toList();
    }

    public void deleteComment(Long postId, Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        comment.validatePost(postId);
        commentRepository.deleteAllByParentComment(comment);
        commentRepository.delete(comment);
    }

    private Map<Long, CommentResponse> getCommentResponseMap(Long postId, List<Comment> comments) {
        var commentMap = new HashMap<Long, CommentResponse>();
        var commentLikeCountMap = commentLikeService.getCommentLikeCount(postId);

        for (var comment : comments) {
            var likeCount = commentLikeCountMap.getOrDefault(comment.getId(), 0);
            var commentResponse = getCommentResponseWithComment(comment.getMember().getId(), comment, likeCount);
            if (comment.getParentComment() == null) {
                commentMap.put(comment.getId(), commentResponse);
                continue;
            }
            var savedCommentResponse = commentMap.get(comment.getParentComment().getId());
            savedCommentResponse.replies().add(commentResponse);
            commentMap.put(comment.getParentComment().getId(), savedCommentResponse);
        }

        return commentMap;
    }

    private CommentResponse getCommentResponseWithComment(Long memberId, Comment comment, Integer likeCount) {
        var departmentGroupId = comment.getPost()
                .getDepartmentGroup()
                .getId();
        var memberUniversityDepartment = memberUniversityDepartmentService.findProperMemberUniversityDepartment(memberId, departmentGroupId);
        var universityDepartment = memberUniversityDepartment.getUniversityDepartment();
        var memberUniversityInfo = MemberUniversityInfo.of(
                memberUniversityDepartment.getId(),
                memberUniversityDepartment.getMember().getName(),
                universityDepartment.getUniversity(),
                universityDepartment.getDepartment()
        );
        return CommentResponse.of(comment.getId(), memberUniversityInfo, comment.getContents(), likeCount, comment.getCreatedAt(), new ArrayList<>());
    }

    private MemberCommentResponse getMemberCommentResponse(Comment comment) {
        return MemberCommentResponse.of(comment.getId(), comment.getContents(), comment.getCreatedAt());
    }

    private void updateCommentWithCommentUpdateRequest(Comment comment, CommentRequest commentRequest) {
        comment.updateContents(commentRequest.contents());
        commentRepository.save(comment);
    }
}
