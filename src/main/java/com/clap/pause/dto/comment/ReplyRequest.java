package com.clap.pause.dto.comment;

public record ReplyRequest(
        Long parentCommentId,
        String contents
) {
}
