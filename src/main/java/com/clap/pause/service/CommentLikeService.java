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

    public void like(Long commentId, Long memberId) {
        if (commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            throw new InvalidRequestException("이미 좋아요 한 댓글입니다.");
        }
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundElementException(commentId + "를 가진 댓글이 존재하지 않습니다."));
        var member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundElementException(memberId + "를 가진 이용자가 존재하지 않습니다."));
        var commentLike = new CommentLike(comment, member);
        commentLikeRepository.save(commentLike);
    }

    public void dislike(Long commentId, Long memberId) {
        if (!commentLikeRepository.existsByCommentIdAndMemberId(commentId, memberId)) {
            throw new InvalidRequestException("좋아요 하지 않은 댓글입니다.");
        }
        commentLikeRepository.deleteAllByCommentIdAndMemberId(commentId, memberId);
    }

    public Map<Long, Integer> getCommentLikeCount(Long postId) {
        var result = new HashMap<Long, Integer>();
        var commentLikeQueryResults = commentLikeRepository.getCommentLikeQueryResults(postId);
        for (var query : commentLikeQueryResults) {
            result.put(query.commentId(), query.likeCount());
        }
        return result;
    }
}
