package com.clap.pause.dto.postlike;

public record PostLikeResponse(
        Integer likeCount,
        Boolean liked
) {
    public static PostLikeResponse of(Integer likeCount, Boolean liked) {
        return new PostLikeResponse(likeCount, liked);
    }
}
