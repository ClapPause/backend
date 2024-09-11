package com.clap.pause.dto.post.response;

public record ImageVoteOptionResponse(
        Long id,
        String image,
        String description
) {
    public static ImageVoteOptionResponse of(Long id, String image, String description) {
        return new ImageVoteOptionResponse(id, image, description);
    }
}
