package com.clap.pause.dto.post.request;

import jakarta.validation.constraints.NotBlank;

public record ImageVoteOptionRequest(
        @NotBlank(message = "이미지 선택지에 이미지가 반드시 선택되어야 합니다.")
        String image,
        @NotBlank(message = "이미지에 대한 설명이 필요합니다.")
        String description
) {
}
