package com.clap.pause.repository;

import com.clap.pause.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostIdOrderByCreatedAt(Long postId);

    List<Comment> findAllByMemberIdOrderByCreatedAt(Long memberId);
}

