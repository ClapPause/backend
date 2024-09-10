package com.clap.pause.dto.post.response;

public record PostIdResponse(
        Long id
) {
    public static PostIdResponse of(Long id) {
        return new PostIdResponse(id);
    }

}
