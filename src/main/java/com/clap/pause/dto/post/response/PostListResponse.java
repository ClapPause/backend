package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String contents,
        PostCategory postCategory,
        PostType postType,
        LocalDateTime createdAt,
        String memberName,
        String university,
        String department
) {
    public static PostListResponse of(Long id, String title, String contents, PostCategory postCategory,
                                      PostType postType,
                                      LocalDateTime createdAt, String memberName, String university,
                                      String department) {
        return new PostListResponse(id, title, contents, postCategory, postType, createdAt, memberName, university,
                department);
    }
}
