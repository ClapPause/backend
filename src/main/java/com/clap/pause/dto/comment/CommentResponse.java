package com.clap.pause.dto.comment;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        MemberUniversityInfo memberUniversityInfo,
        String contents,
        LocalDateTime createdAt
) {
    public static CommentResponse of(Long id, MemberUniversityInfo memberUniversityInfo, String contents, LocalDateTime createdAt) {
        return new CommentResponse(id, memberUniversityInfo, contents, createdAt);
    }
}
