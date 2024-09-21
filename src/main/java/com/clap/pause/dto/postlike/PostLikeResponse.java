package com.clap.pause.dto.postlike;

public record PostLikeResponse(
        int likeCount,
        boolean liked
) {
    public static PostLikeResponse of(int likeCount, boolean liked) {
        return new PostLikeResponse(likeCount, liked);
    }
}
