package com.clap.pause.repository;

import com.clap.pause.model.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

    void deleteAllByCommentIdAndMemberId(Long commentId, Long memberId);
}
