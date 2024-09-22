package com.clap.pause.dto.comment;

public record CommentSaveResponse(
        Long id,
        String contents
) {
    public static CommentSaveResponse of(Long id, String contents) {
        return new CommentSaveResponse(id, contents);
    }
}
