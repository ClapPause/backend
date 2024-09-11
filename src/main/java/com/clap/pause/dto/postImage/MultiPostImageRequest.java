package com.clap.pause.dto.postImage;

import com.clap.pause.model.Post;

import java.util.List;

public record MultiPostImageRequest(
        Post post,
        List<String> images
) {
}
