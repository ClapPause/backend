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

    /**
     * 댓글을 저장하는 메서드
     *
     * @param memberId
     * @param postId
     * @param commentRequest
     */
    public void saveComment(Long memberId, Long postId, CommentRequest commentRequest) {
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundElementException(postId + "를 가진 게시글이 존재하지 않습니다."));
        var comment = new Comment(member, post, commentRequest.contents());
        commentRepository.save(comment);
    }

    /**
     * 댓글에 대한 대댓글 저장하는 메서드
     *
     * @param memberId
     * @param postId
     * @param parentCommentId
     * @param commentRequest
     */
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

    /**
     * 입력된 댓글의 내용을 변경하는 메서드
     *
     * @param id
     * @param postId
     * @param commentRequest
     */
    public void updateComment(Long id, Long postId, CommentRequest commentRequest) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        comment.validatePost(postId);
        updateAndSaveComment(comment, commentRequest);
    }

    /**
     * 입력된 Post ID 에 해당하는 댓글 및 대댓글을 한번에 조회하여 시간순으로 정렬한 후 반환하는 메서드
     *
     * @param postId
     * @return result
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> getComments(Long postId) {
        var comments = commentRepository.findAllByPostIdOrderByCreatedAt(postId);
        var commentResponseMap = getCommentResponseMap(postId, comments);

        var result = new ArrayList<>(commentResponseMap.values());
        result.sort(Comparator.comparing(CommentResponse::createdAt));
        return result;
    }

    /**
     * 입력된 Member Id 를 바탕으로 해당 멤버가 작성한 댓글을 시간순으로 정렬하여 반환하는 메서드
     *
     * @param memberId
     */
    @Transactional(readOnly = true)
    public List<MemberCommentResponse> getCommentsWithMember(Long memberId) {
        var comments = commentRepository.findAllByMemberIdOrderByCreatedAt(memberId);

        return comments.stream()
                .map(this::getMemberCommentResponse)
                .toList();
    }

    /**
     * 댓글을 삭제하는 메서드
     *
     * @param postId
     * @param id
     */
    public void deleteComment(Long postId, Long id) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundElementException(id + "를 가진 댓글이 존재하지 않습니다."));
        comment.validatePost(postId);
        commentRepository.delete(comment);
    }

    /**
     * 댓글을 (댓글 ID, 댓글 DTO) 형태의 Map 으로 저장
     * 댓글 DTO 에는 대댓글 DTO 가 저장될 수 있는 형태의 Map 을 반환한다.
     *
     * @param postId
     * @param comments
     * @return commentMap
     */
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

    /**
     * 댓글과 댓글 좋아요 수를 함께 조회할 수 있는 DTO 로 변환하여 반환하는 메서드
     *
     * @param memberId
     * @param comment
     * @param likeCount
     * @return commentResponse
     */
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

    /**
     * 댓글을 MemberCommentResponse DTO 로변환하여 반환하는 메서드
     *
     * @param comment
     */
    private MemberCommentResponse getMemberCommentResponse(Comment comment) {
        return MemberCommentResponse.of(comment.getId(), comment.getContents(), comment.getCreatedAt());
    }

    /**
     * 댓글의 내용을 바꿔 저장하는 메서드
     *
     * @param comment
     * @param commentRequest
     */
    private void updateAndSaveComment(Comment comment, CommentRequest commentRequest) {
        comment.updateContents(commentRequest.contents());
        commentRepository.save(comment);
    }
}
