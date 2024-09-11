package com.clap.pause.dto.post.response;

public record TextVoteOptionResponse(
        Long id,
        String text
) {
    public static TextVoteOptionResponse of(Long id, String text) {
        return new TextVoteOptionResponse(id, text);
    }
}
