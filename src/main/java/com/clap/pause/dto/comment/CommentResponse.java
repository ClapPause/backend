package com.clap.pause.dto.comment;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;

import java.time.LocalDateTime;
import java.util.List;

public record CommentResponse(
        Long id,
        MemberUniversityInfo memberUniversityInfo,
        String contents,
        LocalDateTime createdAt,
        List<ReplyResponse> replies
) {
    public static CommentResponse of(Long id, MemberUniversityInfo memberUniversityInfo, String contents, LocalDateTime createdAt, List<ReplyResponse> replies) {
        return new CommentResponse(id, memberUniversityInfo, contents, createdAt, replies);
    }
}
