package com.clap.pause.dto.postImage;

public record PostImageResponse(
        Long postId,
        String image
) {
    public static PostImageResponse of(Long postId, String image) {
        return new PostImageResponse(postId, image);
    }
}
