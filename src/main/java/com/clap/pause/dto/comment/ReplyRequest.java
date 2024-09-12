package com.clap.pause.dto.comment;

public record ReplyRequest(
        Long postId,
        Long parentCommentId,
        String contents
) {
}
