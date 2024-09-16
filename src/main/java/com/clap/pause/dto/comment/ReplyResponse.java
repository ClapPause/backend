package com.clap.pause.dto.comment;

import com.clap.pause.dto.memberUniversityDepartment.MemberUniversityInfo;

import java.time.LocalDateTime;

public record ReplyResponse(
        Long id,
        MemberUniversityInfo memberUniversityInfo,
        String contents,
        LocalDateTime createdAt
) {
    public static ReplyResponse of(Long id, MemberUniversityInfo memberUniversityInfo, String contents, LocalDateTime createdAt) {
        return new ReplyResponse(id, memberUniversityInfo, contents, createdAt);
    }
}
