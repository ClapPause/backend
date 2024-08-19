package com.clap.pause.dto.post;

import jakarta.validation.constraints.NotBlank;

public record ImageVoteOptionRequest(
        @NotBlank(message = "이미지는 반드시 선택되어야 합니다..")
        String imageUrl,
        @NotBlank(message = "이미지 제목은 최소 1자 이상이어야 합니다.")
        String title
) {
}
