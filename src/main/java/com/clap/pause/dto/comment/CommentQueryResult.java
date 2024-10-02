package com.clap.pause.dto.comment;

import com.clap.pause.model.Comment;

public record CommentQueryResult(
        Comment comment,
        Integer count
) {
    public static CommentQueryResult of(Comment comment, Integer count) {
        return new CommentQueryResult(comment, count);
    }
}
