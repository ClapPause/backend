package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        Long departmentGroupId,
        String title,
        String contents,
        PostCategory postCategory,
        PostType postType,
        LocalDateTime createdAt,
        String memberName,
        String university,
        String department
) {
    public static PostListResponse of(Long id, Long departmentGroupId, String title, String contents,
                                      PostCategory postCategory,
                                      PostType postType,
                                      LocalDateTime createdAt, String memberName, String university,
                                      String department) {
        return new PostListResponse(id, departmentGroupId, title, contents, postCategory, postType, createdAt,
                memberName, university,
                department);
    }
}
