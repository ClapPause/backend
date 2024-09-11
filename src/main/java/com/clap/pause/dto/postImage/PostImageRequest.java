package com.clap.pause.dto.postImage;

import com.clap.pause.model.Post;

public record PostImageRequest(
        Post post,
        String image
) {
}
