package com.clap.pause.dto.commentLike;

public record CommentLikeResponse(
        Integer likeCount,
        Boolean liked
) {
    public static CommentLikeResponse of(Integer likeCount, Boolean liked) {
        return new CommentLikeResponse(likeCount, liked);
    }
}
