package com.clap.pause.dto.vote;

public record VoteOptionCount(
        Long voteOptionId,
        Long count
) {
    public static VoteOptionCount of(Long voteOptionId, Long count) {
        return new VoteOptionCount(voteOptionId, count);
    }
}
