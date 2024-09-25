package com.clap.pause.repository.custom;

import com.clap.pause.dto.comment.CommentQueryResult;

import java.util.List;

public interface CommentCustomRepository {
    List<CommentQueryResult> getCommentQueryResults(Long postId);
}
