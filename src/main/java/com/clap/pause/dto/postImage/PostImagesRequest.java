package com.clap.pause.dto.postImage;

import com.clap.pause.model.Post;

import java.util.List;

public record PostImagesRequest(
        Post post,
        List<String> images
) {
}
