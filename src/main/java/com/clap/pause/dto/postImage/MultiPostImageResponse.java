package com.clap.pause.dto.postImage;

import java.util.List;

public record MultiPostImageResponse(
        Long postId,
        List<String> images
) {
    public static MultiPostImageResponse of(Long postId, List<String> images) {
        return new MultiPostImageResponse(postId, images);
    }
}
