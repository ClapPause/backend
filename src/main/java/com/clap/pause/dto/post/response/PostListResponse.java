package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import java.time.LocalDateTime;
import java.util.List;

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
        String department,
        //options는 글 타입에 따라 달라진다. default일 때는 null, textVote 일 때는 텍스트선택지이고 imageVote일때는 선택지의 설명이 된다.
        List<String> options
) {
    public static PostListResponse of(Long id, Long departmentGroupId, String title, String contents, PostCategory postCategory, PostType postType, LocalDateTime createdAt, String memberName, String university, String department, List<String> options) {
        return new PostListResponse(id, departmentGroupId, title, contents, postCategory, postType, createdAt, memberName, university, department, options);
    }
}
