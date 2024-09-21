package com.clap.pause.dto.postlike;

public record PostLikeResponse(
        boolean liked
) {
    public static PostLikeResponse of(boolean liked) {
        return new PostLikeResponse(liked);
    }
}
