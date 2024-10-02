package com.clap.pause.dto.commentLike;

public record CommentLikeQueryResult(
        Long commentId,
        Integer likeCount
) {
    public static CommentLikeQueryResult of(Long commentId, Integer likeCount) {
        return new CommentLikeQueryResult(commentId, likeCount);
    }
}
