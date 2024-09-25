package com.clap.pause.repository;

import com.clap.pause.model.Comment;
import com.clap.pause.repository.custom.CommentCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentCustomRepository {
    List<Comment> findAllByPostIdOrderByCreatedAt(Long postId);

    void deleteAllByParentComment(Comment parentComment);
}
