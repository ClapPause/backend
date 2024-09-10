package com.clap.pause.dto.post.request;

import com.clap.pause.dto.post.request.ImageVoteOptionRequest;
import com.clap.pause.model.PostCategory;
import com.clap.pause.model.PostType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ImageVoteRequest(
        @NotBlank(message = "제목은 최소 1자 이상이어야 합니다.")
        String title,
        @NotBlank(message = "내용은 최소 1자 이상이어야 합니다.")
        String contents,
        @NotNull(message = "카테고리는 반드시 선택되어야 합니다.")
        PostCategory postCategory,
        @NotNull(message = "글 타입은 반드시 선택되어야 합니다.")
        PostType postType,
        @NotBlank(message = "이미지 투표 항목은 최소 하나 이상 존재해야 합니다.")
        List<String> descriptions
) {
}
