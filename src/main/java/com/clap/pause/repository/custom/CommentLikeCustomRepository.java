package com.clap.pause.repository.custom;

import com.clap.pause.dto.commentLike.CommentLikeQueryResult;

import java.util.List;

public interface CommentLikeCustomRepository {
    List<CommentLikeQueryResult> getCommentLikeQueryResults(Long postId);
}
