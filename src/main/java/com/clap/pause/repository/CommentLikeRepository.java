package com.clap.pause.repository;

import com.clap.pause.model.CommentLike;
import com.clap.pause.repository.custom.CommentLikeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeCustomRepository {
    boolean existsByCommentIdAndMemberId(Long commentId, Long memberId);

    void deleteAllByCommentIdAndMemberId(Long commentId, Long memberId);
}
