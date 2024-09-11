package com.clap.pause.dto.postImage;

import java.util.List;

public record PostImagesResponse(
        Long postId,
        List<String> images
) {
    public static PostImagesResponse of(Long postId, List<String> images) {
        return new PostImagesResponse(postId, images);
    }
}
