package com.clap.pause.dto.post;

import com.clap.pause.model.PostCategory;

import java.time.LocalDateTime;

public record DefaultPostResponse(
        Long id,
        String title,
        String contents,
        PostCategory postCategory,
        LocalDateTime createdAt
) {
    public static DefaultPostResponse of(Long id, String title, String contents, PostCategory postCategory, LocalDateTime createdAt) {
        return new DefaultPostResponse(id, title, contents, postCategory, createdAt);
    }
}
