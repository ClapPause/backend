package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String memberName,
        String university,
        String title,
        String contents,
        PostCategory postCategory,
        PostType postType,
        LocalDateTime createdAt
) {
}
