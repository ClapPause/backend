package com.clap.pause.dto.vote;

import java.util.List;

public record VoteResponse(
        Long totalCount,
        List<VoteOptionCount> voteOptionCounts
) {
    public static VoteResponse of(Long totalCount, List<VoteOptionCount> voteOptionCounts) {
        return new VoteResponse(totalCount, voteOptionCounts);
    }
}
