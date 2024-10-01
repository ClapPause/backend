package com.clap.pause.service;

import com.clap.pause.exception.InvalidRequestException;
import com.clap.pause.exception.NotFoundElementException;
import com.clap.pause.model.CommentLike;
import com.clap.pause.repository.CommentLikeRepository;
import com.clap.pause.repository.CommentRepository;
import com.clap.pause.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentLikeService {
    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;

    /**
     * 댓글 좋아요 하는 기능
     *
     * @param commentId
     * @param memberId
     */
    public void like(Long commentId, Long memberId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundElementException(commentId + "를 가진 댓글이 존재하지 않습니다."));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        if (commentLikeRepository.existsByCommentAndMember(comment, member)) {
            throw new InvalidRequestException("이미 좋아요 한 댓글입니다.");
        }
        var commentLike = new CommentLike(comment, member);
        commentLikeRepository.save(commentLike);
    }

    /**
     * 댓글 좋아요 취소하는 기능
     *
     * @param commentId
     * @param memberId
     */
    public void dislike(Long commentId, Long memberId) {
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundElementException(commentId + "를 가진 댓글이 존재하지 않습니다."));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        if (!commentLikeRepository.existsByCommentAndMember(comment, member)) {
            throw new InvalidRequestException("좋아요 하지 않은 댓글입니다.");
        }
        commentLikeRepository.deleteAllByCommentAndMember(comment, member);
    }

    /**
     * 입력된 Post Id 에 해당하는 댓글 및 대댓글의 좋아요 수를 (ID : 좋아요 수) 형태로 저장하여 Map 형태로 반환하는 메서드
     *
     * @param postId
     * @return result
     */
    public Map<Long, Integer> getCommentLikeCount(Long postId) {
        var result = new HashMap<Long, Integer>();
        var commentLikeQueryResults = commentLikeRepository.getCommentLikeQueryResults(postId);
        for (var query : commentLikeQueryResults) {
            result.put(query.commentId(), query.likeCount());
        }
        return result;
    }
}
