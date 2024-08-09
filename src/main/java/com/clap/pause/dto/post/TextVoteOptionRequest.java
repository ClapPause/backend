package com.clap.pause.dto.post;

import jakarta.validation.constraints.NotBlank;

public record TextVoteOptionRequest(
        @NotBlank(message = "투표 항목은 최소 1자 이상이어야 합니다.")
        String option
) {
}
