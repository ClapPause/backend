package com.clap.pause.repository;

import com.clap.pause.model.Comment;
import com.clap.pause.model.CommentLike;
import com.clap.pause.model.Member;
import com.clap.pause.repository.custom.CommentLikeCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, CommentLikeCustomRepository {
    boolean existsByCommentAndMember(Comment comment, Member member);

    void deleteAllByCommentAndMember(Comment comment, Member member);
}
