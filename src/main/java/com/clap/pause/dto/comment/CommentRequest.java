package com.clap.pause.dto.comment;

public record CommentRequest(
        Long postId,
        String contents
) {
}
