package com.clap.pause.dto.post.request;

import jakarta.validation.constraints.NotNull;

public record PostListRequest(
        @NotNull(message = "학과그룹이 필요합니다.")
        Long departmentGroupId
) {
}
