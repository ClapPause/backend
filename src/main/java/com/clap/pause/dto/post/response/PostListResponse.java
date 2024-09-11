package com.clap.pause.dto.post.response;

import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

        List<byte[]> images,
        List<TextVoteOptionResponse> textVoteOptionResponses,
        List<ImageVoteOptionResponse> imageVoteOptionResponses

) {
    public static PostListResponse of(Long id, Long departmentGroupId, String title, String contents, PostCategory postCategory, PostType postType, LocalDateTime createdAt, String memberName, String university, String department, List<byte[]> images, List<TextVoteOptionResponse> textVoteOptionResponses, List<ImageVoteOptionResponse> imageVoteOptionResponses) {

        return new PostListResponse(id, departmentGroupId, title, contents, postCategory, postType, createdAt, memberName, university, department, images, textVoteOptionResponses, imageVoteOptionResponses);
    }
}
