package com.clap.pause.dto.comment;

import java.time.LocalDateTime;

public record MemberCommentResponse(
        Long id,
        String contents,
        LocalDateTime createdAt
) {
    public static MemberCommentResponse of(Long id, String contents, LocalDateTime createdAt) {
        return new MemberCommentResponse(id, contents, createdAt);
    }
}
