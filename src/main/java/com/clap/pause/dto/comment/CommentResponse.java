package com.clap.pause.dto.comment;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long id,
        MemberUniversityInfo memberUniversityInfo,
        String contents,
        Integer likeCount,
        LocalDateTime createdAt,
        List<CommentResponse> replies
) {
    public static CommentResponse of(Long id, MemberUniversityInfo memberUniversityInfo, String contents, Integer likeCount, LocalDateTime createdAt, List<CommentResponse> replies) {
        return new CommentResponse(id, memberUniversityInfo, contents, likeCount, createdAt, replies);
    }
}
