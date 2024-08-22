package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import java.time.LocalDateTime;

public record PostResponse(
        Long id,
        String title,
        String contents,
        PostCategory postCategory,
        PostType postType,
        Long departmentGroupId,
        LocalDateTime createdAt
) {
    public static PostResponse of(Long id, String title, String contents, PostCategory postCategory, PostType postType,
                                  Long departmentGroupId, LocalDateTime createdAt) {
        return new PostResponse(id, title, contents, postCategory, postType, departmentGroupId, createdAt);
    }
}
